package com.example.administrator.myapplication2;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.myapplication2.Adapter.IntegralAdapter;
import com.example.administrator.myapplication2.Bean.IntegralData;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;

public class Integral extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private TextView integral_sum;
    private ListView resourceScreen;

    private JSONObject results;
    private JSONObject result;
    private JSONObject JO;
    private List<IntegralData> resources;
    private IntegralAdapter integralAdapter;
    final Handler handler = new MyHandler();
    private HttpUtil httpUtil;
    private HashMap<String, String> params;
    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);

        back = (ImageView) findViewById(R.id.back);
        integral_sum = (TextView) findViewById(R.id.integral_sum);
        resourceScreen = (ListView) findViewById(R.id.resourceScreen);
        httpUtil = new HttpUtil();

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        try{
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayIntegral();
    }

    private void displayIntegral(){
        final UserInfo UI = (UserInfo) getApplication();
        params = new HashMap<String, String>();
        params.put("sql","select integral_sum from user where id = "+UI.getId());
        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/displaySql.do",params,new MyStringCallBack() {
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

        params = new HashMap<String, String>();
        params.put("sql","select score,source,time from integral where u_id = "+UI.getId()+" order by time desc");
        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/displaySql.do",params,new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                Log.e("response:",response+" "+id);
                if(!response.equals("{}")){
                    Message message = new Message();
                    message.what = 1;
                    message.obj = response;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 1://更新积分详情
                        results = new JSONObject(msg.obj.toString());
                        resources = new LinkedList<IntegralData>() ;
                        resources.add(new IntegralData("积分","积分来源","获得时间"));
                        for(int i = 0;i<results.length();i++){
                            JO = results.getJSONObject(""+i);
                            date = new Date(new Long(JO.getString("time")));
                            resources.add(new IntegralData(JO.getString("score"),JO.getString("source"),simpleDateFormat.format(date)));
                        }
                        integralAdapter = new IntegralAdapter(Integral.this,resources);
                        resourceScreen.setAdapter(integralAdapter);
                        break;
                    case 2://更新总积分
                        result = new JSONObject(msg.obj.toString());
                        integral_sum.setText(getResources().getString(R.string.integral_sum)+":"+result.getJSONObject("0").getString("integral_sum"));
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
