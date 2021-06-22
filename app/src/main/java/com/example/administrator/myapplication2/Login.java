package com.example.administrator.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Login extends AppCompatActivity implements View.OnClickListener{
    private EditText account;
    private EditText password;
    private ImageView login;
    private TextView forget;
    private TextView register;
    private Intent intent;
    private HttpUtil httpUtil;
    private HashMap<String,String> params;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //判断用户信息和登陆状态
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("id", null) != null){
            signIn();
        }

        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        login = (ImageView) findViewById(R.id.login);
        forget = (TextView) findViewById(R.id.forget_password);
        register = (TextView) findViewById(R.id.register);
        httpUtil = new HttpUtil();
        login.setOnClickListener(this);
        forget.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        try{
            switch (v.getId()){
                case R.id.login:
                    if (TextUtils.isEmpty(account.getText()) || TextUtils.isEmpty(password.getText())) {
                        Toast.makeText(Login.this,"账号或密码不可为空！",Toast.LENGTH_SHORT).show();
                    }else{
                        if(!CheckMobilePhoneNum(account.getText().toString())) {
                            Toast.makeText(Login.this, "请输入正确手机号格式！", Toast.LENGTH_SHORT).show();
                        }else{
                            params = new HashMap<String ,String>();
                            params.put("telephone",account.getText().toString());
                            params.put("password",password.getText().toString());
                            httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/passwordLogin.do",params,new MyStringCallBack(){
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Toast.makeText(Login.this,"登录失败！",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    if(response.equals("Password error")){
                                        Toast.makeText(Login.this,"密码错误！",Toast.LENGTH_SHORT).show();
                                        password.setHint("请输入正确密码！");
                                        password.setText(null);
                                    }else if (response.equals("Unregistered")){
                                        Toast.makeText(Login.this,"该手机号未被注册！",Toast.LENGTH_SHORT).show();
                                    }else{
                                        try {
                                            JSONObject JO = new JSONObject(response);
                                            sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("id", JO.getString("id"));
                                            editor.putString("user_name", JO.getString("name"));
                                            editor.putString("img", JO.getString("img_name"));
                                            editor.putString("telephone", JO.getString("telephone"));
                                            editor.commit();
                                            signIn();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                    break;
                case R.id.forget_password:
                    intent = new Intent(Login.this,AnswerPasswordQuestion.class);
                    startActivity(intent);
                    break;
                case R.id.register:
                    intent = new Intent(Login.this,RegisterActivity.class);
                    startActivity(intent);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void signIn(){
        final UserInfo UI = (UserInfo)getApplication();
        UI.setId(sharedPreferences.getString("id", null));
        UI.setName(sharedPreferences.getString("user_name", null));
        UI.setImg(sharedPreferences.getString("img", null));
        UI.setTelephone(sharedPreferences.getString("telephone", null));

        intent = new Intent(Login.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
