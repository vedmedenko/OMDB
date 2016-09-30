package com.dataroottrainee.rxomdb.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.dataroottrainee.rxomdb.R;

public final class DialogFactory {

    public static Dialog createSimpleOkErrorDialog(@NonNull Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkErrorDialog(@NonNull Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(context.getString(R.string.dialog_general_error_Message))
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    private DialogFactory() {
        throw new AssertionError("No instances.");
    }
}
