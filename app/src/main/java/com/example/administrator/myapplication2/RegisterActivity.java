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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText name;
    private EditText telephone;
    private EditText password;
    private EditText password1;
    private Button register;
    private ImageView back;
    private HttpUtil httpUtil;
    private HashMap<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        telephone = (EditText) findViewById(R.id.telephone);
        password = (EditText) findViewById(R.id.password);
        password1 = (EditText) findViewById(R.id.password1);
        register = (Button) findViewById(R.id.register);
        back = (ImageView) findViewById(R.id.back);
        register.setOnClickListener(this);
        back.setOnClickListener(this);
        httpUtil = new HttpUtil();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.register:
                if(TextUtils.isEmpty(name.getText())){
                    Toast.makeText(RegisterActivity.this,"昵称不可为空！",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(telephone.getText())){
                    Toast.makeText(RegisterActivity.this,"手机号不可为空！",Toast.LENGTH_SHORT).show();
                }else if(!CheckMobilePhoneNum(telephone.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"请输入正确手机号格式！",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(password.getText())){
                    Toast.makeText(RegisterActivity.this,"密码不可为空！",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password1.getText())){
                    Toast.makeText(RegisterActivity.this,"重复密码不可为空！",Toast.LENGTH_SHORT).show();
                }else if(!password.getText().toString().equals(password1.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"两次输入密码不一致！",Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(RegisterActivity.this,"可提交！",Toast.LENGTH_SHORT).show();
                    params = new HashMap<String ,String>();
                    params.put("name",name.getText().toString());
                    params.put("telephone",telephone.getText().toString());
                    params.put("password",password.getText().toString());
                    params.put("birthday","");
                    params.put("age","");
                    params.put("sex","");
                    params.put("job","");
                    params.put("email","");
                    httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/register.do",params,new MyStringCallBack(){
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(RegisterActivity.this,"注册失败！",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            switch (response){
                                case "Name Registered":
                                    Toast.makeText(RegisterActivity.this,"该昵称已被注册！",Toast.LENGTH_SHORT).show();
                                    break;
                                case "Telephone Registered":
                                    Toast.makeText(RegisterActivity.this,"该手机号已被注册！",Toast.LENGTH_SHORT).show();
                                    break;
                                case "false":
                                    Toast.makeText(RegisterActivity.this,"注册失败！",Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                            }
                        }
                    });
                }
                break;
        }
    }

    public static boolean CheckMobilePhoneNum(String phoneNum) {
        String regex = "^(1[3-9]\\d{9}$)";
        if (phoneNum.length() == 11) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phoneNum);
            if (m.matches()) {
                return true;
            }
        }
        return false;
    }
}
