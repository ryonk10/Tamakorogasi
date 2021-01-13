package com.example.tmakorogasi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class GameOverWindowFragment extends DialogFragment {
    public int gameLevel;
    GameOverWindowFragment(int level){
        gameLevel=level;
    }
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.setCancelable(false);
        builder.setTitle("GAME OVER...");
        builder.setMessage("がんばれ");
        builder.setPositiveButton("リトライ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentRetry=new Intent(getActivity(),SettingActivity.class);
                intentRetry.putExtra("gameLevel",gameLevel);
                startActivity(intentRetry);
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
