package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import MyClass.CustomVideoView;
import MyClass.HttpUtil;
import MyClass.InternetRequest;
import MyClass.MyStringCallBack;

import android.os.Handler;
import android.os.Message;

public class OtherCognition extends AppCompatActivity {
    private ImageView back;
    private ImageView pic;
    private TextView text;
    private CustomVideoView video;
    private MediaController mediaController;
    private ListView others;
    private Intent intent;
    private JSONObject info;
    private InternetRequest IR;
    private JSONObject results;
    private JSONObject JO;
    private String str;
    private List<OtherCognitions> resources;
    private OtherCognitionsAdapter oca;
    private ThreadPoolExecutor tpe;
    private Lis lis;
    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //yyyy-MM-dd :HH:mm:ss
    private Message message;
    final Handler handler = new MyHandler();
    private HttpUtil httpUtil;
    private Map<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_cognition);

        back = (ImageView)findViewById(R.id.back);
        pic = (ImageView)findViewById(R.id.pic);
        text = (TextView)findViewById(R.id.text);
        text.setMovementMethod(ScrollingMovementMethod.getInstance());
        video = (CustomVideoView)findViewById(R.id.video);
        others = (ListView)findViewById(R.id.others);
        resources = new LinkedList<OtherCognitions>() ;
        lis = new Lis();
        pic.setOnClickListener(lis);
        back.setOnClickListener(lis);

        intent = this.getIntent();
        httpUtil = new HttpUtil();
        try{
            info = new JSONObject(intent.getStringExtra("info"));

            if(info.getString("type").equals("视频")){
                text.setVisibility(View.GONE);
                pic.setVisibility(View.GONE);
                video.setVisibility(View.VISIBLE);
                setVideo();
            }else if(info.getString("type").equals("文本")){
                video.setVisibility(View.GONE);
                pic.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
                params = new HashMap<String, String>();
                params.put("file",info.getString("file"));
                httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        super.onResponse(response, id);
                        text.setText(response);
                    }
                });
            }else{
                text.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                pic.setVisibility(View.VISIBLE);
                Glide.with(OtherCognition.this).load("http://59.110.215.154:8080/resource/"+info.getString("file")).into(pic);
            }

            IR = new InternetRequest();
        }catch (Exception e){
            e.printStackTrace();
        }

        tpe = new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(128));

        displayCognition();

        others.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                try{
//                    Log.e("XXX","setOnItemClickListener");
//                    Toast.makeText(OtherCognition.this,"你点击了第" + i + "项",Toast.LENGTH_SHORT).show();

                    intent = new Intent(OtherCognition.this,CognitionDetail.class);
                    intent.putExtra("info", results.getJSONObject(""+i).toString());
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayCognition();
    }

    private void displayCognition(){
        tpe.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    IR.addPara("ID",info.getString("id"));
                    str = IR.requestPost("http://59.110.215.154:8080/CognitionAPP/displayCognition.do");
                    results = new JSONObject(str);
                    resources = new LinkedList<OtherCognitions>() ;
                    for(int i = 0;i<results.length();i++){
                        JO = results.getJSONObject(""+i);
                        date = new Date(new Long(JO.getString("time")));
                        resources.add(new OtherCognitions(JO.getString("id"),JO.getString("u_id"),JO.getString("u_img"),JO.getString("u_name"),JO.getString("file"),simpleDateFormat.format(date),JO.getString("good"),JO.getString("comment"),"1"));
                    }
                    message = Message.obtain();
                    message.what = 1;
//                    message.obj = str;
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setVideo(){
        video.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                //开始播放之后消除背景
                video.setBackground(null);
            }
            @Override
            public void onPause() {
                System.out.println("Pause!");//our needed process when the video is paused
            }
        });

        String videoUrl = null;
        try {
            videoUrl = "http://59.110.215.154:8080/resource/"+info.getString("file");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        //video.setVideoURI(Uri.parse("http://59.110.215.154:8080/resource/2019跨年.mp4" ));
        video.setVideoPath(videoUrl);
        //设置视频缩略图
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoUrl, new HashMap());
        bitmap = retriever.getFrameAtTime();
        video.setBackground(new BitmapDrawable(getResources(),bitmap));
        mediaController = new MediaController(this);
        video.setMediaController(mediaController);
        mediaController.setMediaPlayer(video);
    }

    public class Lis implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Log.e("XXX","onClick");
            switch (v.getId()) {
                case R.id.pic:
//                    Toast.makeText(OtherCognition.this,"pic",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.back:
                    finish();
                    break;
            }
            Log.e("XXX","onClickSuccessfully");
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    oca = new OtherCognitionsAdapter(OtherCognition.this,resources,0);
                    others.setAdapter(oca);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
