package com.example.administrator.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import MyClass.UserInfo;


//import static android.os.Build.VERSION_CODES.R;

public class MainActivity extends AppCompatActivity {

    private EditText etName;
    private Button btn;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        etName = (EditText) findViewById(R.id.name);
        btn = (Button) findViewById(R.id.join_btn);
        final UserInfo UI = (UserInfo)getApplication();
        UI.setTpe(new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(128)));
        SharedPreferences sharedPreferences = getSharedPreferences("user", PictureStory.MODE_PRIVATE);
        String user_name = sharedPreferences.getString("user_name", null);
        if(user_name != null){
            etName.setHint(user_name);
            UI.setName(user_name);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                UI.setName(name);
                UI.setId("1");
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_name", name);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, DescribeScene.class);
//                Intent intent = new Intent(MainActivity.this, Test.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}