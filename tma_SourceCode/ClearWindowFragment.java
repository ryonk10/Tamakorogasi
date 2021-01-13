package com.example.tmakorogasi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ClearWindowFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.setCancelable(false);
        builder.setTitle("GAME CLEAR!!");
        builder.setMessage("おめでとうございます！");
        builder.setPositiveButton("タイトルへ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentTitle = new Intent(getActivity(), MainActivity.class);
                startActivity(intentTitle);
            }
        });
        builder.setNegativeButton("設定画面へ戻る", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentBack = new Intent(getActivity(), SettingActivity.class);
                startActivity(intentBack);
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
