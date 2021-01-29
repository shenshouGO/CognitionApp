package com.example.administrator.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

public class GameStart extends AppCompatActivity {
    private TextView etName;
    private TextView etImg;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_start);

        etName = (TextView) findViewById(R.id.username);
        etImg = (TextView) findViewById(R.id.userimg);

        intent=this.getIntent();
        etName.setText(intent.getStringExtra("name"));
        etImg.setText(intent.getStringExtra("img"));
    }
}
