package com.example.android.digit_span.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.android.digit_span.AudiTestEntranceActivity;
import com.example.android.digit_span.R;
import com.example.android.digit_span.VisTestEntranceActivity;

/**
 * Created by 刘博林 on 7/25/2017.
 */

public class Alert extends AlertDialog {
    public static final int NO_NAME_VISUAL = 0;
    public static final int CHECK_NAME_VISUAL = 1;
    public static final int CHECK_NAME_AUDITORY = 2;
    public static final int NO_NAME_AUDITORY = 3;
    public static final int NO_MODE_SELECTED= 4;
    public static final int EXIT_HALFWAY = 5;

    public Alert(Context context) {
        super(context);
    }

    public void ShowAlertDialog(final int alert, final String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        switch (alert) {
            case NO_NAME_AUDITORY:
            case NO_NAME_VISUAL:
                builder.setMessage(R.string.no_name_alert).setPositiveButton("SART WITH OUT NAME", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (alert == NO_NAME_AUDITORY) {
                            getContext().startActivity(new Intent(getContext(), AudiTestEntranceActivity.class));
                        } else {
                            getContext().startActivity(new Intent(getContext(), VisTestEntranceActivity.class));
                        }

                    }
                });
                break;
            case CHECK_NAME_AUDITORY:
            case CHECK_NAME_VISUAL:
                builder.setMessage("Check your name: "+name).setPositiveButton("I HAVE CHECKED", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (alert == CHECK_NAME_AUDITORY) {
                            getContext().startActivity(new Intent(getContext(), AudiTestEntranceActivity.class).putExtra("name",name));
                        } else {
                            getContext().startActivity(new Intent(getContext(), VisTestEntranceActivity.class).putExtra("name",name));
                        }
                    }
                });

                break;
            case NO_MODE_SELECTED:
                builder.setMessage(R.string.no_mode_alert);
                break;
            default:
                throw new IllegalArgumentException();


        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void ShowAlertDialog(final int alert){
        ShowAlertDialog(alert,null);
    }
}
