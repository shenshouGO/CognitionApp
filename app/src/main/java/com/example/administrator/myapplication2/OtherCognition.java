package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication2.Adapter.OtherCognitionsAdapter;
import com.example.administrator.myapplication2.Bean.OtherCognitions;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import MyClass.InternetRequest;
import android.os.Handler;
import android.os.Message;

public class OtherCognition extends AppCompatActivity {
    private ImageView back;
    private ImageView send;
    private ImageView pic;
    private TextView text;
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
    private RatingBar validity;
    private RatingBar novelty;
    private Lis lis;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //yyyy-MM-dd :HH:mm:ss
    private Message message;
    final Handler handler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_cognition);

        back = (ImageView)findViewById(R.id.back);
        send = (ImageView)findViewById(R.id.send);
        pic = (ImageView)findViewById(R.id.pic);
        text = (TextView)findViewById(R.id.text);
        others = (ListView)findViewById(R.id.others);
        resources = new LinkedList<OtherCognitions>() ;
        lis = new Lis();
        pic.setOnClickListener(lis);

        intent = this.getIntent();
        try{
            info = new JSONObject(intent.getStringExtra("info"));
            Glide.with(OtherCognition.this).load("http://47.95.197.189:8080/file/"+info.getString("file")).into(pic);
            Log.e("show successfully:","");

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
                    str = IR.requestPost("http://47.95.197.189:8080/CognitionAPP/displayCognition.do");
                    results = new JSONObject(str);
                    resources = new LinkedList<OtherCognitions>() ;
                    for(int i = 0;i<results.length();i++){
                        JO = results.getJSONObject(""+i);
                        date = new Date(new Long(JO.getString("time")));
                        resources.add(new OtherCognitions(JO.getString("id"),JO.getString("u_img"),JO.getString("u_name"),JO.getString("file"),simpleDateFormat.format(date),JO.getString("good"),JO.getString("comment"),"1"));
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

    public class Lis implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Log.e("XXX","onClick");
            switch (v.getId()) {
                case R.id.pic:
                    Toast.makeText(OtherCognition.this,"pic",Toast.LENGTH_SHORT).show();
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
                    oca = new OtherCognitionsAdapter(OtherCognition.this,resources);
                    others.setAdapter(oca);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
