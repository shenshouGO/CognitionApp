package com.example.administrator.myapplication2;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication2.Adapter.HorizontalListViewAdapter;
import com.example.administrator.myapplication2.Adapter.ScreenAdapter;
import com.example.administrator.myapplication2.Bean.Resource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import MyClass.HorizontalListView;
import MyClass.HttpUtil;
import MyClass.InternetRequest;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;

public class PictureStory extends AppCompatActivity {
    private Button b;
    private ImageView iv;
    private String path;
    private Context context;
    private Dialog dialog;
    private Bitmap bmp;
    private ImageView imageView;
    private boolean big;
    private Intent intent;
    private Bundle bundle;
    private HorizontalListView resourceList;
    private HorizontalListViewAdapter hListViewAdapter;
    private List<Resource> resources;
    private ListView lv;
    private ScreenAdapter sa;
    private List<Resource> screens;
    private InternetRequest IR;
    private JSONObject results;
    private JSONObject JO;
    private String str;
    private HttpUtil httpUtil;
    final Handler handler = new MyHandler();
    private Message message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_story);
        Log.e("onCreate","XXX");
        httpUtil = new HttpUtil();
        showActivity();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","XXX");
    }
    @Override
    protected void onPause() {
        Log.e("onPause","XXX");
        super.onPause();
    }
    @Override
    public void onDestroy() {
        Log.e("onDestroy","XXX");
        super.onDestroy();
    }

    private void showActivity(){
        resourceList = (HorizontalListView)findViewById(R.id.resourceList);
        lv = (ListView) findViewById(R.id.resourceScreen);
        resources = new LinkedList<Resource>() ;
        screens = new LinkedList<Resource>() ;
        path = "http://47.95.197.189:8080/file/";

        httpUtil.postRequest("http://47.95.197.189:8080/CognitionAPP/displayCognitionResource.do",null,new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                try {
                    results = new JSONObject(response);
                    for(int i = 0;i<results.length();i++){
                        JO = results.getJSONObject(""+i);
                        resources.add(new Resource(path+JO.getString("file"),"TOP"+(i+1)));
                        screens.add(new Resource(path+JO.getString("file"),JO.getString("theme")+"|"+JO.getString("type")+"|最新"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                message = Message.obtain();
                message.what = 1;
//                    message.obj = str;
                handler.sendMessage(message);
            }
        });

        resourceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(PictureStory.this,"你点击了第" + i + "项",Toast.LENGTH_SHORT).show();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                try{
                    Intent intent = new Intent(PictureStory.this, Cognition.class);
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
                    hListViewAdapter = new HorizontalListViewAdapter(PictureStory.this, (List<Resource>) resources);
                    resourceList.setAdapter(hListViewAdapter);
                    sa = new ScreenAdapter(PictureStory.this, (List<Resource>) screens);
                    lv.setAdapter(sa);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
