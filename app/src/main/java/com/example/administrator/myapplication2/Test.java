package com.example.administrator.myapplication2;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test extends AppCompatActivity {
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
        setContentView(R.layout.test);

        path = "http://47.95.197.189:8080/file/timg.jpg";
        context = Test.this;

        b = (Button) findViewById(R.id.b);
        iv = (ImageView) findViewById(R.id.iv);

        intent=this.getIntent();
        final String name = intent.getStringExtra("name");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FileUtils fu = new FileUtils();
                fu.downLoad(path,"maidang.jpg");
                Toast.makeText(Test.this,"正在下载！",Toast.LENGTH_SHORT).show();
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

                Glide.with(Test.this).load("http://47.95.197.189:8080/file/"+name).into(iv);
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Test.this,ImageDetail.class);
                intent.putExtra("path",path);
                intent.putExtra("img","maidang.jpg");
                startActivity(intent);
            }
        });
    }
}