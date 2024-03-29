package com.example.administrator.myapplication2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.myapplication2.Adapter.OtherCognitionsAdapter;
import com.example.administrator.myapplication2.Bean.OtherCognitions;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;

public class DescribeScene extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private ImageView add;
    private ListView resourceScreen;

    private JSONObject results;
    private JSONObject JO;
    private List<OtherCognitions> resources;
    private OtherCognitionsAdapter oca;
    final Handler handler = new MyHandler();
    private HttpUtil httpUtil;
    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_scene);

        back = (ImageView) findViewById(R.id.back);
        add = (ImageView) findViewById(R.id.add);
        resourceScreen = (ListView) findViewById(R.id.resourceScreen);
        httpUtil = new HttpUtil();

        back.setOnClickListener(this);
        add.setOnClickListener(this);
        resourceScreen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                try{
                    intent = new Intent(DescribeScene.this,SceneDetail.class);
                    intent.putExtra("info", results.getJSONObject(""+i).toString());
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        try{
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.add:
                    Intent intent = new Intent(DescribeScene.this, ReleaseScene.class);
                    intent.putExtra("type","发布情景");
                    startActivity(intent);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayScene();
    }

    private void displayScene(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("sql","select * from cognition_resource where unit = 1 and type = '情景' order by time desc");
        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/displaySql.do",params,new MyStringCallBack() {
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
                if (msg.what == 1) {
                    results = new JSONObject(msg.obj.toString());
                    resources = new LinkedList<OtherCognitions>() ;
                    for(int i = 0;i<results.length();i++){
                        JO = results.getJSONObject(""+i);
                        date = new Date(new Long(JO.getString("time")));
                        resources.add(new OtherCognitions(JO.getString("id"),JO.getString("u_id"),JO.getString("u_img"),JO.getString("u_name"),JO.getString("file"),simpleDateFormat.format(date),JO.getString("good"),JO.getString("comment"),"1"));
                    }
                    oca = new OtherCognitionsAdapter(DescribeScene.this,resources,1);
                    resourceScreen.setAdapter(oca);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
