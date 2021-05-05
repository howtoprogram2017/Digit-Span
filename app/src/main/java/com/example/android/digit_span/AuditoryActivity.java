package com.example.android.digit_span;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.digit_span.data.TestContract;
import com.example.android.digit_span.data.TestContract.TestEntry;
import com.example.android.digit_span.utility.DStest;
import com.example.android.digit_span.utility.Digit;
import com.example.android.digit_span.utility.HelpDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.drawable.btn_star;
import static android.R.drawable.btn_star_big_on;

/**
 * Created by 刘博林 on 7/25/2017.
 */

public class AuditoryActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    private final int FORWARD_PRETEST=0,FORWARD_TEST=1,BACKWARD_PRETEST=2,BACKWARD_TEST=3;
    private final int START_LEVEL=3;
    //default step
    int CurrentStep=FORWARD_PRETEST;
    int EndStep=BACKWARD_TEST;
    String TesterName;
    Boolean needSave=true;
    Boolean currentFished=false;
    private static final String TAG = "AuditoryActivity";
    FloatingActionButton floatingActionButton;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    ImageButton refresh;
    TextView guideWords;
    LinearLayout button_row1;
    LinearLayout button_row2;
    ImageView star[];
    Button start_play;
    List<Integer> GivenDigits,
            DigitVoice;    //voice resources of digits in sequence
    List<Digit> mDigits;  //10 digit sounds;
    List<Integer> AnswerDigits;
    MediaPlayer mediaPlayer;
    TextView answer;
    Handler handler = new Handler();
    int CurrentLevel=START_LEVEL;
    DStest mtest;
    int inputnum=0;
    int addLevel=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.auditorytest);
        setupValues();


        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpDialog helpDialog=new HelpDialog(AuditoryActivity.this);
                helpDialog.showhelp(helpDialog.AUDI_FORWAR_PRETEST);
            }
        });
        guideWords=(TextView)findViewById(R.id.guidewords);
        if(CurrentStep==BACKWARD_PRETEST){
            this.setTitle("Auditory Backward Pretest");
            guideWords.setText("Prepare for Test \nClick in the inverse order");
        }
        start_play=(Button)findViewById(R.id.audi_start_play);
        start_play.setOnClickListener(new_sequence);
        button0=(Button) findViewById(R.id.digit0);
        button1=(Button) findViewById(R.id.digit1);
        button2=(Button) findViewById(R.id.digit2);
        button3=(Button) findViewById(R.id.digit3);
        button4=(Button) findViewById(R.id.digit4);
        button5=(Button) findViewById(R.id.digit5);
        button6=(Button) findViewById(R.id.digit6);
        button7=(Button) findViewById(R.id.digit7);
        button8=(Button) findViewById(R.id.digit8);
        button9=(Button) findViewById(R.id.digit9);
        button_row1=(LinearLayout)findViewById(R.id.button_row1);
        button_row2=(LinearLayout)findViewById(R.id.button_row2);
        refresh=(ImageButton)findViewById(R.id.refresh);
        answer=(TextView)findViewById(R.id.entered_digit) ;
        star=new ImageView[3];
        star[0]=(ImageView) findViewById(R.id.star1);
        star[1]=(ImageView) findViewById(R.id.star2);
        star[2]=(ImageView) findViewById(R.id.star3);
        refresh.setOnClickListener(refreshListener);
        button0.setOnClickListener(DigitOnClickListener(0));
        button1.setOnClickListener(DigitOnClickListener(1));
        button2.setOnClickListener(DigitOnClickListener(2));
        button3.setOnClickListener(DigitOnClickListener(3));
        button4.setOnClickListener(DigitOnClickListener(4));
        button5.setOnClickListener(DigitOnClickListener(5));
        button6.setOnClickListener(DigitOnClickListener(6));
        button7.setOnClickListener(DigitOnClickListener(7));
        button8.setOnClickListener(DigitOnClickListener(8));
        button9.setOnClickListener(DigitOnClickListener(9));

    }

    private void setupValues() {
        Digit.FindResoucre();
        mDigits=Digit.mdigit;
        Intent intent=getIntent();
        Log.d(TAG, "setupValues: "+intent.getIntExtra(TestContract.MODE,-1));
        switch (intent.getIntExtra(TestContract.MODE,-1)){
            case TestContract.FORWARD_BACKWARD:
                break;
            case TestContract.BACKWARD_ONLY:
                CurrentStep=BACKWARD_PRETEST;
                break;
            case TestContract.FORWARD_ONLY:
                EndStep=FORWARD_TEST;
                break;
            default:
                throw new IllegalArgumentException();
        };

        if(!intent.hasExtra(TestEntry.TESTER_NAME)) {
            needSave=false;
        }else {
            TesterName=intent.getStringExtra(TestEntry.TESTER_NAME);
        }
        mtest=new DStest(START_LEVEL);
    }

    View.OnClickListener refreshListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           resetTest();
            refresh.setClickable(false);
            ViewPropertyAnimator viewPropertyAnimator=refresh.animate().rotationBy(360f);
            viewPropertyAnimator.setDuration(1000).setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        /**
                         * {@inheritDoc}
                         *
                         * @param animation
                         */
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            refresh.setClickable(true);
                        }
                    });
        }
    };
    View.OnClickListener new_sequence=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: start");
            inputnum=0;
            Random random = new Random();
            AnswerDigits=new ArrayList<Integer>();
            GivenDigits=new ArrayList<Integer>();
            DigitVoice=new ArrayList<Integer>();
            start_play.setClickable(false);
            start_play.animate().alpha(0f).setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)).setListener(new AnimatorListenerAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param animation
                 */
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    start_play.setVisibility(View.INVISIBLE);
                }
            });


            for(int i=0;i<CurrentLevel;i++){
                Digit digit=mDigits.get(random.nextInt(10));
                GivenDigits.add(digit.i);
                DigitVoice.add(digit.mAudioResource);
            }
            for(int i=0;i<CurrentLevel-1;i++) {
                final int ii=i;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer = MediaPlayer.create(AuditoryActivity.this, DigitVoice.get(ii));
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mediaPlayer.release();
                            }
                        });
                    }
                },1000*ii);

            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer = MediaPlayer.create(AuditoryActivity.this, DigitVoice.get(CurrentLevel-1));
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(AuditoryActivity.this);
                }
            },1000*(CurrentLevel-1));
            if(CurrentStep==BACKWARD_PRETEST||CurrentStep==BACKWARD_TEST){
                DStest.inverse(GivenDigits);
            }
            Log.d(TAG, "onClick: before setcorrect");
            mtest.setcorrectanswer(GivenDigits);
        }


    };
    View.OnClickListener DigitOnClickListener(final int digit){
        return (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.append(String.valueOf(digit));
                AnswerDigits.add(digit);
                inputnum++;
                if(inputnum==CurrentLevel){
                    stopClick();
                    inputnum=0;
                    answer.setText("");
                    mtest.setplayeranswer(AnswerDigits);
                    if(mtest.isWin()){
                        star[mtest.getStage()-1].setImageResource(btn_star_big_on);
                        Toast.makeText(AuditoryActivity.this,"You win this stage!",Toast.LENGTH_SHORT).show();
                    }else {
                        if(mtest.isLostLevel()){
                            if(addLevel==2){
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ComeToNextlevel();
                                    }
                                }, 1000);
                            }
                            currentFished=true;
                            if((CurrentStep==FORWARD_TEST||CurrentStep==BACKWARD_TEST)&&needSave==true){
                                saveToDataBase(mtest);
                            }
                            if(CurrentStep==FORWARD_TEST){
                                invalidateOptionsMenu();
                            }
                            Toast.makeText(AuditoryActivity.this,"Test finshed!\nYou achieved Level "+String.valueOf(CurrentLevel-1),Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(AuditoryActivity.this,"You lost this stage!",Toast.LENGTH_SHORT).show();
                    };

                    if(mtest.isWinLevel()){
                        Toast.makeText(AuditoryActivity.this,"you win this level!",Toast.LENGTH_SHORT).show();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ComeToNextlevel();
                            }
                        }, 1000);

                    }else {
                        TextView tv=(TextView)findViewById(R.id.level_label);
                        start_play.setClickable(true);
                        start_play.setVisibility(View.VISIBLE);
                        start_play.setAlpha(1f);
                    }
                }
            }
        });

    }

    private void saveToDataBase(DStest test) {
        ContentValues values=new ContentValues();
        values.put(TestEntry.TESTER_NAME,TesterName);
        values.put(TestEntry.ISAUDITORY,1);
        values.put(TestEntry.ISFORWARD,CurrentStep==FORWARD_TEST?1:0);
        values.put(TestEntry.ALLRESULT,test.saveasjson());
        values.put(TestEntry.TOP,test.topLevel);
        Uri uri=getContentResolver().insert(TestContract.CONTENT_URI,values);
        if(uri==null){
            handler.postDelayed(new Runnable() {
                public void run() {
                    Toast.makeText(AuditoryActivity.this,"Test saved failed",Toast.LENGTH_SHORT).show();
                }

            },1000);
        }else{
            Toast.makeText(AuditoryActivity.this, "Your test result has been saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopClick() {
        button_row1.setVisibility(View.INVISIBLE);
        button_row2.setVisibility(View.INVISIBLE);
        button_row1.setClickable(false);
        button_row2.setClickable(false);
    }

    private void ComeToNextlevel() {
        addLevel=mtest.addlevel();
        CurrentLevel+=addLevel;
        TextView tv=(TextView)findViewById(R.id.level_label);
        tv.setText(String.valueOf(CurrentLevel)+" Words");
        start_play.setClickable(true);
        start_play.setAlpha(1f);
        start_play.setVisibility(View.VISIBLE);
        for(int i=0;i<3;i++){
            star[i].setImageResource(btn_star);
        }
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        button_row1.setVisibility(View.VISIBLE);
        button_row2.setVisibility(View.VISIBLE);
        button_row2.setAlpha(0f);
        button_row1.setAlpha(0f);
        button_row1.animate().alpha(1f).setDuration(100).start();
        button_row2.animate().alpha(1f).start();
        button_row1.setClickable(true);
        button_row2.setClickable(true);
        mediaPlayer.release();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.skip, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.nextstep:
                Toast.makeText(AuditoryActivity.this,"set",Toast.LENGTH_SHORT);

                refresh.setClickable(true);
                refresh.setVisibility(View.VISIBLE);
                Nextstep();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void Nextstep() {
        currentFished=false;
        CurrentStep++;
        resetTest();
        String newTitle;
        String guideWord;
        switch (CurrentStep){
            case FORWARD_TEST:
                newTitle="Forward Test";
                guideWord="Recorded Test\nClick in the same order";
                refresh.setVisibility(View.INVISIBLE);
                refresh.setClickable(false);
                invalidateSkip();
                break;
            case BACKWARD_TEST:
                refresh.setVisibility(View.INVISIBLE);
                guideWord="Recorded Test\nClick in inverse order";
                refresh.setClickable(false);
                newTitle="Backward Test";
                invalidateSkip();
                break;
            case BACKWARD_PRETEST:
                newTitle="Backward Pretest";
                refresh.setClickable(true);
                guideWord="Prepare for Test \nClick in inverse order";
                refresh.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalArgumentException();
        }
        setTitle("Auditory "+newTitle);
        guideWords.setText(guideWord);


    }
    private void resetTest()
    {   addLevel=0;
        CurrentLevel = START_LEVEL;
        answer.setText("");
        stopClick();
        mtest = new DStest(START_LEVEL);
        TextView tv = (TextView) findViewById(R.id.level_label);
        tv.setText(String.valueOf(CurrentLevel) + " Words");
        start_play.setClickable(true);
        start_play.setVisibility(View.VISIBLE);
        start_play.setAlpha(1f);
        for (int i = 0; i < 3; i++) {
            star[i].setImageResource(btn_star);
        }
    }
    private void invalidateSkip(){
            refresh.setClickable(false);
            refresh.setVisibility(View.INVISIBLE);
            invalidateOptionsMenu();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem menuItem = menu.findItem(R.id.nextstep);
        if (CurrentStep==BACKWARD_PRETEST||CurrentStep==FORWARD_PRETEST||(CurrentStep==FORWARD_TEST&&currentFished)) {
            menuItem.setVisible(true);
        }else if(!currentFished&&(CurrentStep==FORWARD_TEST||CurrentStep==BACKWARD_TEST)){
            menuItem.setVisible(false);
        }
        return true;
    }
}
