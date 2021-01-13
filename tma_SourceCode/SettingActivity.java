package com.example.tmakorogasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int gameLevel= getIntent().getIntExtra("gameLevel",0);
        if(gameLevel==1){
            Intent intent1=new Intent(this,Level1Activity.class);
            startActivity(intent1);
        }else if(gameLevel==2){
            Intent intent2=new Intent(this,Level2Activity.class);
            startActivity(intent2);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }
    public void onClick_buttonLevel1(View view){
        Intent intent1=new Intent(this,Level1Activity.class);
        startActivity(intent1);
    }

    public void onClick_buttonLevel2(View view){
        Intent intent2=new Intent(this,Level2Activity.class);
        startActivity(intent2);
    }
}