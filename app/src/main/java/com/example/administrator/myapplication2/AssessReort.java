package com.example.administrator.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.myapplication2.Adapter.AssessAdapter;
import com.example.administrator.myapplication2.Bean.Resource;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;

/**
 * Created by Administrator on 2021/3/4.
 */

public class AssessReort extends AppCompatActivity implements View.OnClickListener{
    private HttpUtil httpUtil;
    private Map<String,String> params;
    final Handler handler = new MyHandler();
    private Message message;
    private Intent intent;
    private JSONObject info;
    private JSONObject results;
    private List<Resource> resources;
    private AssessAdapter aa;
    private JSONObject JO;

    private ImageView back;
    private TextView title;
    private ImageView add;
    private ListView resourceScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_scene);

        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.assessReporter));
        add = (ImageView) findViewById(R.id.add);
        add.setVisibility(View.GONE);
        resourceScreen = (ListView) findViewById(R.id.resourceScreen);
        httpUtil = new HttpUtil();
        back.setOnClickListener(this);

        params = new HashMap<String, String>();
        params.put("type","评估");
        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/displayVideo.do",params,new MyStringCallBack() {
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

        resourceScreen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                try{
                    intent = new Intent(AssessReort.this,AssessDetail.class);
                    intent.putExtra("info", results.getJSONObject(""+i).toString());
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
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
                    resources = new LinkedList<Resource>() ;
                    for(int i = 0;i<results.length();i++){
                        JO = results.getJSONObject(""+i);
                        resources.add(new Resource(JO.getString("file"),"","",""));
                    }
                    aa = new AssessAdapter(AssessReort.this,resources);
                    resourceScreen.setAdapter(aa);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
}
