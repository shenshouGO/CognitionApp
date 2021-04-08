package com.example.administrator.myapplication2;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import MyClass.CustomVideoView;
import MyClass.GetuiFunction;
import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.SHAEncryption;

public class Test extends AppCompatActivity implements View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{
    private static final String TAG = Test.class.getSimpleName();
    private CustomVideoView video;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        video = (CustomVideoView)findViewById(R.id.video);

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
        videoUrl = "http://192.168.154.1:8080/file/你的答案.mp4";
//        try {
//            videoUrl = "http://192.168.154.1:8080/file/"+info.getString("file");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                GetuiFunction.getToken(new MyStringCallBack(){
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG,response);
                    }
                });
                break;
            default:
                break;
        }
    }
}