package com.example.administrator.myapplication2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;

public class ImageDetail extends AppCompatActivity {
    private ImageView iv;
    private Intent intent;
    private Bitmap bmp;
    private String path;
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail);

        iv = (ImageView) findViewById(R.id.iv);
        intent = getIntent();
        path = intent.getStringExtra("path");
        img = intent.getStringExtra("img");
        try{
            bmp = BitmapFactory.decodeStream(new FileInputStream(new File(path, img)));
        }catch (Exception e){
            e.printStackTrace();
        }
        iv.setImageBitmap(bmp);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
