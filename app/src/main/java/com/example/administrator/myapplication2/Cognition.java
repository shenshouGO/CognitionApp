package com.example.administrator.myapplication2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MyClass.CustomVideoView;
import MyClass.HttpUtil;
import MyClass.InternetRequest;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;

public class Cognition extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private ImageView send;
    private ImageView pic;
    private TextView text;
    private CustomVideoView video;
    private MediaController mediaController;
    private EditText cognition;
    private TextView name;
    private TextView words;
    private TextView draw;
    private Intent intent;
    private JSONObject info;

    private HttpUtil httpUtil;
    private Map<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cognition);

        back = (ImageView)findViewById(R.id.back);
        send = (ImageView)findViewById(R.id.send);
        pic = (ImageView)findViewById(R.id.pic);
        text = (TextView)findViewById(R.id.text);
        video = (CustomVideoView)findViewById(R.id.video);
        cognition = (EditText) findViewById(R.id.cognition);
        cognition.setVisibility(View.INVISIBLE);
        name = (TextView)findViewById(R.id.name);
        words = (TextView)findViewById(R.id.words);
        draw = (TextView)findViewById(R.id.draw);
        draw.setVisibility(View.INVISIBLE);

        Drawable[] nameD = name.getCompoundDrawables();
        Drawable[] wordsD = words.getCompoundDrawables();
        Drawable[] drawD = draw.getCompoundDrawables();
        nameD[1].setBounds(0,0,50,45);
        wordsD[1].setBounds(0,0,50,50);
        drawD[1].setBounds(0,0,50,50);
        name.setCompoundDrawables(nameD[0],nameD[1],nameD[2],nameD[3]);
        words.setCompoundDrawables(wordsD[0],wordsD[1],wordsD[2],wordsD[3]);
        draw.setCompoundDrawables(drawD[0],drawD[1],drawD[2],drawD[3]);
        back.setOnClickListener(this);
        name.setOnClickListener(this);
        words.setOnClickListener(this);
        pic.setOnClickListener(this);
        draw.setOnClickListener(this);

        httpUtil = new HttpUtil();
        intent = this.getIntent();
        final UserInfo UI = (UserInfo)getApplication();
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
                httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
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
                draw.setVisibility(View.VISIBLE);
                Glide.with(Cognition.this).load("http://192.168.154.1:8080/file/"+info.getString("file")).into(pic);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(cognition.getText())){
                    Toast.makeText(Cognition.this,"请输入内容再发送！",Toast.LENGTH_SHORT).show();
                }else{
                    final String data = cognition.getText().toString();
                    cognition.setText("");
                    try {
                        params = new HashMap<String, String>();
                        params.put("ID",UI.getId());
                        params.put("u_name",UI.getName());
                        params.put("u_img",UI.getImg());
                        params.put("c_r_id",info.getString("id"));
                        params.put("c_r_file",info.getString("file"));
                        params.put("cognition",data);

                        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
                            @Override
                            public void onResponse(String response, int id) {
                                super.onResponse(response, id);
                                if(response.equals("Create successfully")){
                                    intent = new Intent(Cognition.this, OtherCognition.class);
                                    intent.putExtra("info", info.toString());
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(Cognition.this,"重评失败！",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        try{
            switch (v.getId()) {
                case R.id.name:
                case R.id.words:
                    cognition.setVisibility(View.VISIBLE);
                    break;
                case R.id.draw:
                    ImagePicker.getInstance().setSelectLimit(1);
                    intent = new Intent(Cognition.this, ImageGridActivity.class);
                    startActivity(intent);
                    break;
                case R.id.pic:
                    List<String> imgs = new ArrayList<String>();
                    imgs.add("http://192.168.154.1:8080/file/"+info.getString("file"));
                    intent = new Intent(Cognition.this, PhotoView.class);
                    intent.putStringArrayListExtra("Urls",(ArrayList<String>) imgs);
                    intent.putExtra("currentPosition", 0);
                    startActivity(intent);
                    break;
                case R.id.back:
                    finish();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
            videoUrl = "http://192.168.154.1:8080/file/"+info.getString("file");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        //video.setVideoURI(Uri.parse("http://192.168.154.1:8080/file/2019跨年.mp4" ));
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
}
