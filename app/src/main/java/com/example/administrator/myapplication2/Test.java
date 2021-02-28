package com.example.administrator.myapplication2;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.HashMap;

import MyClass.CustomVideoView;

public class Test extends AppCompatActivity{

    private CustomVideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        bindViews();
    }

    private void bindViews() {
        videoView = (CustomVideoView) findViewById(R.id.videoView);
        videoView.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                //开始播放之后消除背景
                videoView.setBackground(null);
            }
            @Override
            public void onPause() {
                System.out.println("Pause!");//our needed process when the video is paused
            }
        });

        String videoUrl = "http://192.168.154.1:8080/file/人为什么要努力.mp4";
        Bitmap bitmap = null;
        //videoView.setVideoURI(Uri.parse("http://192.168.154.1:8080/file/2019跨年.mp4" ));
        videoView.setVideoPath(videoUrl);
        //设置视频缩略图
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoUrl, new HashMap());
        bitmap = retriever.getFrameAtTime();
        videoView.setBackground(new BitmapDrawable(getResources(),bitmap));
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
    }
}