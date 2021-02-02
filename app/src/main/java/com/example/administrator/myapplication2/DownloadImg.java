package com.example.administrator.myapplication2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import MyClass.FileUtils;

public class DownloadImg extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_img);

        path = "http://47.95.197.189:8080/file/timg.jpg";
        context = DownloadImg.this;

        b = (Button) findViewById(R.id.b);
        iv = (ImageView) findViewById(R.id.iv);

        intent=this.getIntent();
        final String name = intent.getStringExtra("name");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FileUtils fu = new FileUtils();
                fu.downLoad(path,"maidang.jpg");
                Toast.makeText(DownloadImg.this,"正在下载！",Toast.LENGTH_SHORT).show();
                path = "http://47.95.197.189:8080/file/";
//                path = Environment.getExternalStorageDirectory().toString() + "/shidoe";

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            File f = new File(path, "maidang.jpg");
//                            bmp = BitmapFactory.decodeStream(new FileInputStream(f));
//                            iv.setImageBitmap(bmp);
//                            big = false;
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                }, 200);

                Glide.with(DownloadImg.this).load(path+name).into(iv);
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(DownloadImg.this,ImageDetail.class);
                intent.putExtra("path",path);
                intent.putExtra("img",name);
                startActivity(intent);
            }
        });
    }
}
