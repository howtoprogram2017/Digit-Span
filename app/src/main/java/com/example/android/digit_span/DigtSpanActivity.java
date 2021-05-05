package com.example.android.digit_span;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.digit_span.utility.Alert;
import com.example.android.digit_span.utility.HelpDialog;


public class DigtSpanActivity extends AppCompatActivity {
    EditText name;
    Button visual;
    Button auditory;
    private static final String TAG = "DigtSpanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digt_span);
        name = (EditText) findViewById(R.id.name);
         visual = (Button) findViewById(R.id.visual_test);
        auditory = (Button) findViewById(R.id.auditory_test);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpDialog helpDialog = new HelpDialog(DigtSpanActivity.this);
                helpDialog.showhelp(HelpDialog.DIGIT_SPAN_INTRODUCTION);
            }
        });
        visual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentname=name.getText().toString().trim();
                Alert alert=new Alert(DigtSpanActivity.this);
                if(currentname==null||currentname.length()==0){

                    alert.ShowAlertDialog(Alert.NO_NAME_VISUAL,null);
                }else {

                    alert.ShowAlertDialog(Alert.CHECK_NAME_VISUAL,currentname);
                }
            }

        });
        auditory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentname=name.getText().toString().trim();
                Alert alert=new Alert(DigtSpanActivity.this);
                if(currentname==null||currentname.length()==0){
                    alert.ShowAlertDialog(Alert.NO_NAME_AUDITORY,null);
                }else {
                    alert.ShowAlertDialog(Alert.CHECK_NAME_AUDITORY,currentname);
                }
            }
        });


    }
}
