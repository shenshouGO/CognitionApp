package com.example.administrator.myapplication2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class DescribeScene extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private ImageView add;
    private ListView resourceScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_scene);

        back = (ImageView) findViewById(R.id.back);
        add = (ImageView) findViewById(R.id.add);
        resourceScreen = (ListView) findViewById(R.id.resourceScreen);

        back.setOnClickListener(this);
        add.setOnClickListener(this);
        resourceScreen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                try{

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        try{
            switch (v.getId()) {
                case R.id.back:
                    break;
                case R.id.add:
                    Intent intent = new Intent(DescribeScene.this, ReleaseScene.class);
                    startActivity(intent);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
