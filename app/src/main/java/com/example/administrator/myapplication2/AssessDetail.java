package com.example.administrator.myapplication2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;

public class AssessDetail extends AppCompatActivity implements View.OnClickListener{
    private HttpUtil httpUtil;
    private Map<String,String> params;
    final Handler handler = new MyHandler();
    private Message message;
    private Intent intent;
    private JSONObject info;
    private JSONObject results;
    private JSONObject JO;

    private TextView title;
    private ProgressBar progress;
    private TextView question;
    private RadioGroup chooses;
    private RadioButton one;
    private RadioButton two;
    private RadioButton three;
    private RadioButton four;
    private RadioButton five;
    private RadioButton six;
    private RadioButton seven;
    private Button last;
    private Button next;
    private Button submit;
    private RelativeLayout result;
    private TextView result_text;
    private Button again;
    private Button back;
    private String[] questions;
    private int[] choose;
    private int[] score;
    private int current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_detail);

        title = (TextView)findViewById(R.id.title);
        progress = (ProgressBar) findViewById(R.id.progress);
        question = (TextView)findViewById(R.id.question);
        chooses = (RadioGroup)findViewById(R.id.chooses);
        one = (RadioButton)findViewById(R.id.one);
        two = (RadioButton)findViewById(R.id.two);
        three = (RadioButton)findViewById(R.id.three);
        four = (RadioButton)findViewById(R.id.four);
        five = (RadioButton)findViewById(R.id.five);
        six = (RadioButton)findViewById(R.id.six);
        seven = (RadioButton)findViewById(R.id.seven);
        last = (Button)findViewById(R.id.last);
        next = (Button)findViewById(R.id.next);
        submit = (Button)findViewById(R.id.submit);
        result = (RelativeLayout) findViewById(R.id.result);
        result_text = (TextView)findViewById(R.id.result_text);
        again = (Button)findViewById(R.id.again);
        back = (Button)findViewById(R.id.back);

        last.setOnClickListener(this);
        next.setOnClickListener(this);
        submit.setOnClickListener(this);
        again.setOnClickListener(this);
        back.setOnClickListener(this);

        httpUtil = new HttpUtil();
        intent = getIntent();
        try {
            info = new JSONObject(intent.getStringExtra("info"));
            Log.e("info",info.getString("file"));
            String[] split = info.getString("file").split("\\.");
            for(int i = 0;i<split.length;i++){
                Log.e("split",i+" "+split[i]);
            }
            title.setText(split[0]);
            params = new HashMap<String, String>();
            params.put("file",info.getString("file"));
            httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
                @Override
                public void onResponse(String response, int id) {
                    super.onResponse(response, id);
                    questions = response.split("\n");
                    init();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void init(){
        progress.setVisibility(View.VISIBLE);
        question.setVisibility(View.VISIBLE);
        chooses.setVisibility(View.VISIBLE);
        result.setVisibility(View.INVISIBLE);

        choose = new int[questions.length];
        score = new int[questions.length];
        current = 0;

        message = Message.obtain();
        message.what = 1;
        handler.sendMessage(message);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.last:
                if (chooses.getCheckedRadioButtonId()!=-1){
                    countScore();
                }
                current--;
                message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
                break;
            case R.id.next:
                countScore();
                if (chooses.getCheckedRadioButtonId()!=-1){
                    current++;
                    message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);
                }
                break;
            case R.id.submit:
                if (chooses.getCheckedRadioButtonId()!=-1){
//                    Toast.makeText(AssessDetail.this,"可以提交",Toast.LENGTH_SHORT).show();
                    countScore();
                    progress.setVisibility(View.INVISIBLE);
                    question.setVisibility(View.INVISIBLE);
                    chooses.setVisibility(View.INVISIBLE);
                    last.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.INVISIBLE);
                    submit.setVisibility(View.INVISIBLE);

                    result.setVisibility(View.VISIBLE);
                    int scores = 0;
                    for(int i=0;i<score.length;i++){
                        scores+=score[i];
                    }
                    result_text.setText("总得分为"+scores+"分");
                }else{
                    Toast.makeText(AssessDetail.this,"请先进行选择",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.again:
                init();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @SuppressWarnings("ResourceType")
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 1://渲染题目和选项
                        question.setText(questions[current]);
                        chooses.check(-1);
                        if(choose[current]!=0){
                            chooses.check(choose[current]);
                        }
                        progress.setProgress(current);

                        if(current == 0){
                            last.setVisibility(View.INVISIBLE);
                            next.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.INVISIBLE);
                        }else if(current == questions.length-1){
                            last.setVisibility(View.VISIBLE);
                            next.setVisibility(View.INVISIBLE);
                            submit.setVisibility(View.VISIBLE);
                        }else{
                            last.setVisibility(View.VISIBLE);
                            next.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.INVISIBLE);
                        }
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void countScore(){
        switch (chooses.getCheckedRadioButtonId()){
            case R.id.one:
                choose[current] = R.id.one;
                score[current] = 1;
                break;
            case R.id.two:
                choose[current] = R.id.two;
                score[current] = 2;
                break;
            case R.id.three:
                choose[current] = R.id.three;
                score[current] = 3;
                break;
            case R.id.four:
                choose[current] = R.id.four;
                score[current] = 4;
                break;
            case R.id.five:
                choose[current] = R.id.five;
                score[current] = 5;
                break;
            case R.id.six:
                choose[current] = R.id.six;
                score[current] = 6;
                break;
            case R.id.seven:
                choose[current] = R.id.seven;
                score[current] = 7;
                break;
            default:
                Toast.makeText(AssessDetail.this,"请先进行选择",Toast.LENGTH_SHORT).show();
        }
    }
}
