package com.example.whatsappclone.Utills;

import android.app.AlertDialog;
import android.content.Context;

public class NoInternetDialog {
    public static void showNoInternetDialog(Context context) {
        new AlertDialog.Builder(context).setTitle("No Internet Connection").setMessage("Please check your internet connection and try again.").setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
    }
}
