package com.example.administrator.myapplication2;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class Test extends AppCompatActivity implements View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{
    private static final String TAG = Test.class.getSimpleName();

    private static final int REQ_CODE_INIT_APIKEY = 0;

    Button initWithApiKey = null;
    Button setTags = null;
    Button delTags = null;
    Button clearLog = null;
    Button showTags = null;
    Button unbind = null;
    Button setunDisturb = null;
    TextView logText = null;
    ScrollView scrollView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        initWithApiKey = (Button) findViewById(R.id.btn_initAK);
        setTags = (Button) findViewById(R.id.btn_setTags);
        delTags = (Button) findViewById(R.id.btn_delTags);
        clearLog = (Button) findViewById(R.id.btn_clear_log);
        showTags = (Button) findViewById(R.id.btn_showTags);
        unbind = (Button) findViewById(R.id.btn_unbindTags);
        setunDisturb = (Button) findViewById(R.id.btn_setunDisturb);
        logText = (TextView) findViewById(R.id.text_log);
        scrollView = (ScrollView) findViewById(R.id.stroll_text);

        initWithApiKey.setOnClickListener(this);
        setTags.setOnClickListener(this);
        delTags.setOnClickListener(this);
        clearLog.setOnClickListener(this);
        showTags.setOnClickListener(this);
        unbind.setOnClickListener(this);
        setunDisturb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_initAK:
                break;
            default:
                break;
        }
    }
}