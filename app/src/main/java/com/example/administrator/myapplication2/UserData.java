package com.example.administrator.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;

public class UserData extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private TextView nickname;
    private TextView sex;
    private TextView age;
    private TextView birthday;
    private TextView job;
    private TextView telephone;
    private TextView email;
    private Button edit_data;

    private HttpUtil httpUtil;
    private Map<String,String> params;
    private JSONObject JO;
    private Intent intent;
    private Message message;
    final Handler handler = new MyHandler();

    private String userId;
    private String[] sexs = new String[3];
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        back = (ImageView) findViewById(R.id.back);
        nickname = (TextView) findViewById(R.id.nickname);
        sex = (TextView) findViewById(R.id.sex);
        age = (TextView) findViewById(R.id.age);
        birthday = (TextView) findViewById(R.id.birthday);
        job = (TextView) findViewById(R.id.job);
        telephone = (TextView) findViewById(R.id.telephone);
        email = (TextView) findViewById(R.id.email);
        edit_data = (Button) findViewById(R.id.edit_data);

        back.setOnClickListener(this);
        edit_data.setOnClickListener(this);

        httpUtil = new HttpUtil();
        intent = getIntent();
        userId = intent.getStringExtra("userId");
        sexs[0] = "女";
        sexs[1] = "男";
        sexs[2] = "未设置";
    }

    @Override
    protected void onResume(){
        super.onResume();
        display();
    }

    private void display(){
        params = new HashMap<String, String>();
        if (userId.equals("0")){
            final UserInfo UI = (UserInfo)getApplication();
            params.put("sql","select * from user where id = "+UI.getId());
            edit_data.setVisibility(View.VISIBLE);
            edit_data.setText("修改资料");
        }else{
            params.put("sql","select * from user where id = "+userId);
            edit_data.setVisibility(View.GONE);
        }
        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/displaySql.do",params,new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                message = Message.obtain();
                message.what = 1;
                message.obj = response;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.edit_data:
                intent = new Intent(UserData.this, EditUserData.class);
                intent.putExtra("info",JO.toString());
                startActivity(intent);
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
                        JO = new JSONObject(msg.obj.toString());
                        JO = JO.getJSONObject("0");
                        nickname.setText("昵称      "+JO.getString("name"));
                        sex.setText("性别      "+sexs[Integer.parseInt(JO.getString("sex"))]);
                        age.setText("年龄      "+JO.getString("age"));
                        birthday.setText("生日      "+JO.getString("birthday"));
                        job.setText("职业      "+JO.getString("job"));
                        telephone.setText("电话      "+JO.getString("telephone"));
                        email.setText("邮箱      "+JO.getString("email"));

                        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_name", JO.getString("name"));
                        editor.putString("img", JO.getString("img_name"));
                        editor.commit();

                        final UserInfo UI = (UserInfo)getApplication();
                        UI.setName(sharedPreferences.getString("user_name", null));
                        UI.setImg(sharedPreferences.getString("img", null));
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
