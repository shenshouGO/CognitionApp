package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

import MyClass.InternetRequest;
import MyClass.UserInfo;

public class Cognition extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private ImageView send;
    private ImageView pic;
    private TextView text;
    private EditText cognition;
    private TextView name;
    private TextView words;
    private TextView mosaic;
    private TextView draw;
    private Intent intent;
    private String str;
    private JSONObject info;
    private InternetRequest IR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cognition);

        back = (ImageView)findViewById(R.id.back);
        send = (ImageView)findViewById(R.id.send);
        pic = (ImageView)findViewById(R.id.pic);
        text = (TextView)findViewById(R.id.text);
        cognition = (EditText) findViewById(R.id.cognition);
        cognition.setVisibility(View.INVISIBLE);
        name = (TextView)findViewById(R.id.name);
        words = (TextView)findViewById(R.id.words);
        mosaic = (TextView)findViewById(R.id.mosaic);
        draw = (TextView)findViewById(R.id.draw);

        Drawable[] nameD = name.getCompoundDrawables();
        Drawable[] wordsD = words.getCompoundDrawables();
        Drawable[] mosaicD = mosaic.getCompoundDrawables();
        Drawable[] drawD = draw.getCompoundDrawables();
        nameD[1].setBounds(0,0,50,45);
        wordsD[1].setBounds(0,0,50,50);
        mosaicD[1].setBounds(0,0,50,50);
        drawD[1].setBounds(0,0,50,50);
        name.setCompoundDrawables(nameD[0],nameD[1],nameD[2],nameD[3]);
        words.setCompoundDrawables(wordsD[0],wordsD[1],wordsD[2],wordsD[3]);
        mosaic.setCompoundDrawables(mosaicD[0],mosaicD[1],mosaicD[2],mosaicD[3]);
        draw.setCompoundDrawables(drawD[0],drawD[1],drawD[2],drawD[3]);
        name.setOnClickListener(this);
        words.setOnClickListener(this);
        mosaic.setOnClickListener(this);
        draw.setOnClickListener(this);

        intent = this.getIntent();
        final UserInfo UI = (UserInfo)getApplication();
        try{
            info = new JSONObject(intent.getStringExtra("info"));
            Glide.with(Cognition.this).load("http://47.95.197.189:8080/file/"+info.getString("file")).into(pic);

            IR = new InternetRequest();
        }catch (Exception e){
            e.printStackTrace();
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String data = cognition.getText().toString();
                cognition.setText("请输入内容...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IR.addPara("ID","1");
                            IR.addPara("u_name",UI.getName());
                            IR.addPara("u_img","null.jpg");
                            IR.addPara("c_r_id",info.getString("id"));
                            IR.addPara("c_r_file",info.getString("file"));
                            IR.addPara("cognition",data);
                            str = IR.requestPost("http://47.95.197.189:8080/CognitionAPP/createCognition.do");
//                            str = "Create successfully";

                            if(str.equals("Create successfully")){
                                intent = new Intent(Cognition.this, OtherCognition.class);
                                intent.putExtra("info", info.toString());
                                startActivity(intent);
                            }else{
                                Looper.prepare();
                                Toast.makeText(Cognition.this,"重评失败！",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
