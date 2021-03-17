package com.example.administrator.myapplication2;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import MyClass.GetuiFunction;
import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.SHAEncryption;

public class Test extends AppCompatActivity implements View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{
    private static final String TAG = Test.class.getSimpleName();
    private Button btn;
//    private String appkey = "YuPK31YsFr8cEaAjSEAku1";
//    private String timestamp = ""+System.currentTimeMillis();
//    private String masterSecret = "kINn82fIED9DFCyWJANKH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        btn = (Button)findViewById(R.id.btn);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                GetuiFunction.getToken(new MyStringCallBack(){
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG,response);
                    }
                });
                break;
            default:
                break;
        }
    }
}