package com.example.administrator.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;
import okhttp3.Call;

import static com.example.administrator.myapplication2.RegisterActivity.CheckMobilePhoneNum;

public class AnswerPasswordQuestion extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout user_data;
    private LinearLayout question_data;
    private EditText telephone_content;
    private TextView question_content;
    private EditText answer_content;
    private Button OK;
    private ImageView back;
    private HttpUtil httpUtil;
    private HashMap<String,String> params;
    final Handler handler = new MyHandler();
    private Message message;

    private int type;
    private String u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_password_question);

        user_data = (RelativeLayout) findViewById(R.id.user_data);
        question_data = (LinearLayout) findViewById(R.id.question_data);
        telephone_content = (EditText) findViewById(R.id.telephone_content);
        question_content = (TextView) findViewById(R.id.question_content);
        answer_content = (EditText) findViewById(R.id.answer_content);
        OK = (Button) findViewById(R.id.OK);
        back = (ImageView) findViewById(R.id.back);
        OK.setOnClickListener(this);
        back.setOnClickListener(this);
        httpUtil = new HttpUtil();


        type = 0;
        question_data.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.OK:
                if(type == 0){
                    if (TextUtils.isEmpty(telephone_content.getText())) {
                        Toast.makeText(AnswerPasswordQuestion.this, "手机号不可为空！", Toast.LENGTH_SHORT).show();
                    } else if(!CheckMobilePhoneNum(telephone_content.getText().toString())){
                        Toast.makeText(AnswerPasswordQuestion.this,"请输入正确手机号格式！",Toast.LENGTH_SHORT).show();
                    }else{
                        params = new HashMap<String, String>();
                        params.put("sql", "select id from user where telephone = "+telephone_content.getText().toString());
                        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/displaySql.do", params, new MyStringCallBack() {
                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    if (!response.equals("{}")){
                                        JSONObject JO = new JSONObject(response);
                                        u_id = JO.getJSONObject("0").getString("id");
                                        params = new HashMap<String, String>();
                                        params.put("sql", "select question from password_question where u_id = "+u_id);
                                        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/displaySql.do", params, new MyStringCallBack() {
                                            @Override
                                            public void onResponse(String response, int id) {
                                                try {
                                                    if (!response.equals("{}")){
                                                        JSONObject JO = new JSONObject(response);

                                                        message = Message.obtain();
                                                        message.what = 1;
                                                        message.obj = JO.getJSONObject("0").getString("question");
                                                        handler.sendMessage(message);
                                                    }else{
                                                        Toast.makeText(AnswerPasswordQuestion.this,"该帐号未设置密保问题！",Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(AnswerPasswordQuestion.this,"该手机号未注册！",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }else{
                    if (TextUtils.isEmpty(answer_content.getText())) {
                        Toast.makeText(AnswerPasswordQuestion.this, "密保答案不可为空！", Toast.LENGTH_SHORT).show();
                    } else {
//                    Toast.makeText(AnswerPasswordQuestion.this,"可提交！",Toast.LENGTH_SHORT).show();
                        final UserInfo UI = (UserInfo) getApplication();
                        params = new HashMap<String, String>();
                        params.put("u_id", u_id);
                        params.put("answer", answer_content.getText().toString());
                        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/answerPasswordQuestion.do", params, new MyStringCallBack() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(AnswerPasswordQuestion.this, "设置失败！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                switch (response) {
                                    case "Answer correctly":
                                        Intent intent = new Intent(AnswerPasswordQuestion.this, ModifyPassword.class);
                                        intent.putExtra("type", 1);
                                        intent.putExtra("u_id", u_id);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case "Answer error":
                                        Toast.makeText(AnswerPasswordQuestion.this, "密保答案错误！", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                    }
                }
                break;
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 1:
                        type = 1;
                        user_data.setVisibility(View.GONE);
                        question_data.setVisibility(View.VISIBLE);
                        question_content.setText(msg.obj.toString());
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
