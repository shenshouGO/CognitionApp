package com.example.administrator.myapplication2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.myapplication2.Adapter.MyAdapter;
import com.example.administrator.myapplication2.Bean.MyBean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Talk extends AppCompatActivity {

    private RecyclerView rv;
    private EditText et;
    private Button btn;
    private Socket socket;
    private ArrayList<MyBean> list;
    private MyAdapter adapter;
    private Intent intent;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk);

        rv = (RecyclerView) findViewById(R.id.rv);
        et = (EditText) findViewById(R.id.et);
        btn = (Button) findViewById(R.id.btn);
        list = new ArrayList<>();
        adapter = new MyAdapter(this);

        intent=this.getIntent();
        name = intent.getStringExtra("name");
        Log.v("XXX", "name is " + name);

        final Handler handler = new Talk.MyHandler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("59.110.215.154", 10010);
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len;

                    OutputStream outputStream = socket.getOutputStream();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");    //设置日期格式
                    outputStream.write(( "join//" + name + "加入" + "//" + df.format(new Date())).getBytes("utf-8"));
                    outputStream.flush();
                    len = inputStream.read(buffer);
                    String str = new String(buffer, 0, len);
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = str;
                    handler.sendMessage(message);

                    while ((len = inputStream.read(buffer)) != -1) {
                        str = new String(buffer, 0, len);
                        // 发到主线程中 收到的数据
                        message = Message.obtain();
                        message.what = 1;
                        message.obj = str;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    Log.e("XXX", "Exception:"+e.getStackTrace().toString());
                }
            }
        }).start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String data = et.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OutputStream outputStream = socket.getOutputStream();
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");    //设置日期格式
                            outputStream.write((name + "//" + data + "//" + df.format(new Date())).getBytes("utf-8"));
                            outputStream.flush();

                        } catch (IOException e) {
                            Log.e("XXX", "Exception:"+e.getStackTrace().toString());
                        }
                    }
                }).start();
            }
        });

    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                try{
                    if (msg.what == 1) {
                        //
                        int localPort = socket.getLocalPort();
                        String[] split = ((String) msg.obj).split("//");
                        if (split[0].equals("join")){
                            MyBean bean = new MyBean(" ",split[1], split[2], 3);
                            list.add(bean);
                        }else if (split[0].equals(name)) {
                            MyBean bean = new MyBean( "我：",split[1], split[2], 1);
                            list.add(bean);
                        } else {
                            MyBean bean = new MyBean(("来自：" + split[0]),split[1], split[2], 2 );
                            list.add(bean);
                        }

                        // 向适配器set数据
                        adapter.setData(list);
                        rv.setAdapter(adapter);
                        LinearLayoutManager manager = new LinearLayoutManager(Talk.this, LinearLayoutManager.VERTICAL, false);
                        rv.setLayoutManager(manager);
                    }
                }catch (Exception e){
                    Log.e(MainActivity.class.getCanonicalName(), "Exception:"+e.getStackTrace().toString());
                }
        }
    }
}

