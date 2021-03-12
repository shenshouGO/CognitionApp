package com.example.administrator.myapplication2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_splash);
//        PushManager.startWork(SplashActivity.this, PushConstants.LOGIN_TYPE_API_KEY,"0omst29gbwr0fOWP8qM6Q1Vg");

        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    sleep(1000);//使程序休眠1秒
                    Intent it=new Intent(getApplicationContext(),Login.class);//启动MainActivity
//                    Intent it=new Intent(getApplicationContext(),Test.class);
                    startActivity(it);
                    finish();//关闭当前活动
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }
}
