package com.example.administrator.myapplication2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication2.Adapter.AssessScoreAdapter;
import com.example.administrator.myapplication2.Bean.IntegralData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;

public class AssessDetail extends AppCompatActivity implements View.OnClickListener{
    private HttpUtil httpUtil;
    private Map<String,String> params;
    final Handler handler = new MyHandler();
    private Message message;
    private Intent intent;
    private JSONObject info;

    private TextView title;
    private ProgressBar progress;
    private TextView question;
    private RadioGroup chooses;
    private Button last;
    private Button next;
    private Button submit;
    private RelativeLayout result;
    private ListView result_list;
    private JSONObject results;
    private JSONObject JO;
    private List<IntegralData> resources;
    private AssessScoreAdapter assessScoreAdapter;
    private Button again;
    private Button back;
    private String[] questions;
    private int[] choose;
    private int[] score;
    private int current;
    private String u_id;

    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_detail);

        final UserInfo UI = (UserInfo)getApplication();
        u_id = UI.getId();

        title = (TextView)findViewById(R.id.title);
        progress = (ProgressBar) findViewById(R.id.progress);
        question = (TextView)findViewById(R.id.question);
        chooses = (RadioGroup)findViewById(R.id.chooses);
        last = (Button)findViewById(R.id.last);
        next = (Button)findViewById(R.id.next);
        submit = (Button)findViewById(R.id.submit);
        result = (RelativeLayout) findViewById(R.id.result);
        result_list = (ListView) findViewById(R.id.result_list);
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
            httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
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
                    Log.e("Assess score","总得分为"+scores+"分");

                    try {
                        params = new HashMap<String, String>();
                        params.put("a_id",info.getString("id"));
                        params.put("u_id",u_id);
                        params.put("score",scores+"");
                        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/createAssessScore.do",params,new MyStringCallBack() {
                            @Override
                            public void onResponse(String response, int id) {
                                super.onResponse(response, id);
                                if(response.equals("Insert successfully")){
                                    Toast.makeText(AssessDetail.this,"评估成功",Toast.LENGTH_SHORT).show();
                                    try {
                                        params = new HashMap<String, String>();
                                        params.put("sql","select * from assess_score where a_id = "+info.getString("id")+" and u_id = "+u_id+" order by time desc");
                                        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/displaySql.do",params,new MyStringCallBack() {
                                            @Override
                                            public void onResponse(String response, int id) {
                                                super.onResponse(response, id);
                                                Log.e("response:",response+" "+id);
                                                if(!response.equals("{}")){
                                                    Message message = new Message();
                                                    message.what = 2;
                                                    message.obj = response;
                                                    handler.sendMessage(message);
                                                }
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    Toast.makeText(AssessDetail.this,"评估失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    case 2://渲染评估结果
                        results = new JSONObject(msg.obj.toString());
                        resources = new LinkedList<IntegralData>() ;
                        resources.add(new IntegralData("评估得分",null,"得分时间"));
                        for(int i = 0;i<results.length();i++){
                            JO = results.getJSONObject(""+i);
                            date = new Date(new Long(JO.getString("time")));
                            resources.add(new IntegralData(JO.getString("score"),null,simpleDateFormat.format(date)));
                        }
                        assessScoreAdapter = new AssessScoreAdapter(AssessDetail.this,resources);
                        result_list.setAdapter(assessScoreAdapter);
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
