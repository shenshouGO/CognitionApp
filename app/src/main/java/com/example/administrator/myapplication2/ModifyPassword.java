package com.example.administrator.myapplication2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;
import okhttp3.Call;

public class ModifyPassword extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout original_password;
    private View line;
    private EditText original_content;
    private EditText new_content;
    private EditText repeat_content;
    private Button preserve;
    private ImageView back;
    private HttpUtil httpUtil;
    private HashMap<String,String> params;
    private Intent intent;
    private int type;
    private String u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        original_password = (RelativeLayout) findViewById(R.id.original_password);
        line = (View) findViewById(R.id.line);
        original_content = (EditText) findViewById(R.id.original_content);
        new_content = (EditText) findViewById(R.id.new_content);
        repeat_content = (EditText) findViewById(R.id.repeat_content);
        preserve = (Button) findViewById(R.id.preserve);
        back = (ImageView) findViewById(R.id.back);
        preserve.setOnClickListener(this);
        back.setOnClickListener(this);
        httpUtil = new HttpUtil();
        intent = getIntent();
        type = intent.getIntExtra("type",0);
        u_id = intent.getStringExtra("u_id");

        if(type == 1){
            original_password.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.preserve:
                if(type == 0 && TextUtils.isEmpty(original_content.getText())){
                    Toast.makeText(ModifyPassword.this,"原密码不可为空！",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(new_content.getText())){
                    Toast.makeText(ModifyPassword.this,"新密码不可为空！",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(repeat_content.getText())){
                    Toast.makeText(ModifyPassword.this,"重复新密码不可为空！",Toast.LENGTH_SHORT).show();
                }else if(new_content.getText().toString().equals(original_content.getText().toString())){
                    Toast.makeText(ModifyPassword.this,"新密码不能与原密码相同！",Toast.LENGTH_SHORT).show();
                }else if(!new_content.getText().toString().equals(repeat_content.getText().toString())){
                    Toast.makeText(ModifyPassword.this,"两次输入新密码不一致！",Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(ModifyPassword.this,"可提交！",Toast.LENGTH_SHORT).show();
                    final UserInfo UI = (UserInfo)getApplication();
                    params = new HashMap<String ,String>();
                    params.put("u_id",u_id);
                    if(type == 0)
                        params.put("original_password",original_content.getText().toString());
                    params.put("password",new_content.getText().toString());
                    httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/modifyPassword.do",params,new MyStringCallBack(){
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(ModifyPassword.this,"修改密码失败！",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            switch (response){
                                case "Password error":
                                    Toast.makeText(ModifyPassword.this,"原密码错误！",Toast.LENGTH_SHORT).show();
                                    break;
                                case "Modify password successfully":
                                    Toast.makeText(ModifyPassword.this,"修改密码成功！",Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                case "Modify password unsuccessfully":
                                    Toast.makeText(ModifyPassword.this,"修改密码失败！",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                }
                break;
        }
    }
}
