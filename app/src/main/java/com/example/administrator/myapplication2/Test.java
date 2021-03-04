package com.example.administrator.myapplication2;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.HashMap;

import MyClass.CustomVideoView;
import MyClass.HttpUtil;
import MyClass.MyStringCallBack;

public class Test extends AppCompatActivity{
    TextView text;
    HttpUtil httpUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        text = (TextView)findViewById(R.id.text);
        httpUtil = new HttpUtil();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("file","认知焦虑测评.txt");
        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                String[] split = response.split("\n");
                for(int i=0;i<split.length;i++)
                {
                    System.out.println(i+" "+split[i]);
                    Log.e("split",i+" "+split[i]);
                }
            }
        });
    }
}