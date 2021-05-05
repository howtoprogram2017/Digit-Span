package com.example.android.digit_span.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

/**
 * Created by 刘博林 on 7/24/2017.
 */

public class HelpDialog extends AlertDialog {
    public static final String DIGIT_SPAN_INTRODUCTION = "  A digit-span task is used to measure working memory's number storage capacity." +
            " Participants see or hear a sequence of numerical digits and are tasked to recall the sequence correctly," +
            " with increasingly longer sequences being tested in each trial." +
            " The participant's span is the longest number of sequential digits that can accurately be remembered";
    public static final String FORWAR_AND_BACKWARD="  Digit-span tasks can be given forwards or backwards, meaning that once the sequence is presented," +
            " the participant is asked to either recall the sequence in normal or reverse order\n  You can choose either or both of the two modes here.";
    public  static final String AUDI_FORWAR_PRETEST="  BEFORE YOU TAKE AN RECORED TEST, YOU WILL TAKE A PRETEST HERE, WHICH IS ALMOST THE SAME AS THE RECORED TEST"+
                "\n  YOU WILL NEED TO DELIVER THE SEQUENCE OF HEARED NUMBERS, AND YOU HAVE TO CHOOSE THE NUMBERS ONE-TIME WITH NO EDITING"+
            "\n YOU NEED TO SUCCEED TWO TIMES OF A TEST IN THE SAME LEVEL TO UPGRAGE TO THE NEXT LEVEL. THE GAME WILL END IF YOU FAILED TWICE IN CERTAIN LEVEL";

    public HelpDialog(Context context) {
        super(context);
    }

    public void showhelp(String help) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), android.R.style.Theme_Holo_NoActionBar));
        builder.setMessage(help);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
          alertDialog.show();
    }
}
