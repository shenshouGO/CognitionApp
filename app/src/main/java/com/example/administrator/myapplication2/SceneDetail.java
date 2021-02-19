package com.example.administrator.myapplication2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.administrator.myapplication2.Adapter.MixedAdapter;
import com.example.administrator.myapplication2.Adapter.OtherCognitionsAdapter;
import com.example.administrator.myapplication2.Bean.OtherCognitions;

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

public class SceneDetail extends AppCompatActivity {
    private ListView listView;

    private MixedAdapter mixedAdapter;
    private JSONObject results;
    private JSONObject JO;
    private JSONObject info;
    private List<OtherCognitions> resources;
    final Handler handler = new MyHandler();
    private HttpUtil httpUtil;
    private Map<String,String> params;
    private String url;
    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_detail);

        listView = (ListView)findViewById(R.id.list);
        httpUtil = new HttpUtil();
        intent = getIntent();
        try {
            info = new JSONObject(intent.getStringExtra("info"));
            Log.e("Info:",info.toString());
            params = new HashMap<String, String>();
            params.put("ID",info.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayCognition();
    }

    private void displayCognition(){
        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/displayCognition.do",params,new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                Log.e("response:",response+" "+id);
                Message message = new Message();
                message.what = 1;
                message.obj = response;
                handler.sendMessage(message);
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    results = new JSONObject(msg.obj.toString());
                    resources = new LinkedList<OtherCognitions>() ;
                    date = new Date(new Long(info.getString("time")));
                    resources.add(new OtherCognitions(info.getString("id"),info.getString("u_img"),info.getString("u_name"),info.getString("file"),simpleDateFormat.format(date),info.getString("good"),info.getString("comment"),"0"));
                    for(int i = 0;i<results.length();i++){
                        JO = results.getJSONObject(""+i);
                        date = new Date(new Long(JO.getString("time")));
                        resources.add(new OtherCognitions(JO.getString("id"),JO.getString("u_img"),JO.getString("u_name"),JO.getString("file"),simpleDateFormat.format(date),JO.getString("good"),JO.getString("comment"),"1"));
                    }
                    mixedAdapter = new MixedAdapter(SceneDetail.this,resources);
                    listView.setAdapter(mixedAdapter);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
