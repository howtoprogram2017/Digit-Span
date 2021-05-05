package com.example.android.digit_span;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.android.digit_span.data.TestContract;
import com.example.android.digit_span.data.TestContract.TestEntry;
import com.example.android.digit_span.utility.Alert;
import com.example.android.digit_span.utility.HelpDialog;


/**
 * Created by 刘博林 on 7/24/2017.
 */

public class AudiTestEntranceActivity extends AppCompatActivity {
    CheckBox forward;
    CheckBox backward;
    String TesterName;

    //    SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auditory_test_entrance);
        forward = (CheckBox) findViewById(R.id.forward);
        backward = (CheckBox) findViewById(R.id.backward);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpDialog helpDialog = new HelpDialog(AudiTestEntranceActivity.this);
                helpDialog.showhelp(HelpDialog.FORWAR_AND_BACKWARD);
            }
        });
        Button button = (Button) findViewById(R.id.start_test);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isforward = forward.isChecked();
                boolean isbackward = backward.isChecked();
                if (!(isforward || isbackward)) {
                    Alert alert = new Alert(AudiTestEntranceActivity.this);
                    alert.ShowAlertDialog(Alert.NO_MODE_SELECTED);
                } else {
                    Intent intent = new Intent(AudiTestEntranceActivity.this, AuditoryActivity.class);
                    if (getIntent().hasExtra(TestEntry.TESTER_NAME)) {
                        intent.putExtra(TestEntry.TESTER_NAME, getIntent().getStringExtra(TestEntry.TESTER_NAME));
                    }
                    if (!isbackward) {
                        intent.putExtra(TestContract.MODE, TestContract.FORWARD_ONLY);
                    } else if (isforward) {
                        intent.putExtra(TestContract.MODE, TestContract.FORWARD_BACKWARD);
                    } else {
                        intent.putExtra(TestContract.MODE, TestContract.BACKWARD_ONLY);
                    }
                    startActivity(intent);
                }
            }
        });
    }


}
