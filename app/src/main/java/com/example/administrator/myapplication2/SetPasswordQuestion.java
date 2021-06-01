package com.example.administrator.myapplication2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;
import okhttp3.Call;

public class SetPasswordQuestion extends AppCompatActivity implements View.OnClickListener{
    private EditText question_content;
    private EditText answer_content;
    private Button preserve;
    private ImageView back;
    private HttpUtil httpUtil;
    private HashMap<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password_question);

        question_content = (EditText) findViewById(R.id.question_content);
        answer_content = (EditText) findViewById(R.id.answer_content);
        preserve = (Button) findViewById(R.id.preserve);
        back = (ImageView) findViewById(R.id.back);
        preserve.setOnClickListener(this);
        back.setOnClickListener(this);
        httpUtil = new HttpUtil();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.preserve:
                if(TextUtils.isEmpty(question_content.getText())){
                    Toast.makeText(SetPasswordQuestion.this,"密保问题不可为空！",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(answer_content.getText())){
                    Toast.makeText(SetPasswordQuestion.this,"密保答案不可为空！",Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(SetPasswordQuestion.this,"可提交！",Toast.LENGTH_SHORT).show();
                    final UserInfo UI = (UserInfo)getApplication();
                    params = new HashMap<String ,String>();
                    params.put("u_id",UI.getId());
                    params.put("question",question_content.getText().toString());
                    params.put("answer",answer_content.getText().toString());
                    httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/setPasswordQuestion.do",params,new MyStringCallBack(){
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(SetPasswordQuestion.this,"设置失败！",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            switch (response){
                                case "Set successfully":
                                    Toast.makeText(SetPasswordQuestion.this,"设置成功！",Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                case "Set unsuccessfully":
                                    Toast.makeText(SetPasswordQuestion.this,"设置失败！",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                }
                break;
        }
    }
}
