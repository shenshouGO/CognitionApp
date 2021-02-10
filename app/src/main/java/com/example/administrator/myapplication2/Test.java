package com.example.administrator.myapplication2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication2.Adapter.HorizontalListViewAdapter;
import com.example.administrator.myapplication2.Adapter.ScreenAdapter;
import com.example.administrator.myapplication2.Bean.Resource;

import java.util.LinkedList;
import java.util.List;

import MyClass.FileUtils;
import MyClass.HorizontalListView;
import MyClass.InternetRequest;

public class Test extends AppCompatActivity{
    private TextView text;
    private Button b;
    private InternetRequest IR;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        text = (TextView)findViewById(R.id.text);
        b = (Button) findViewById(R.id.b);
        IR = new InternetRequest();

        IR.addPara("telephone","18910002233");
        IR.addPara("password","654");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = IR.requestPost("http://47.95.197.189:8080/CognitionAPP/passwordLogin.do");
                Log.e("XXX","Result:"+result);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        });
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                text.setText(result);
            }
        };

    };
}
