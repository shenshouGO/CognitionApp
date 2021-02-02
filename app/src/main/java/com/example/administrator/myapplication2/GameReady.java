package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.administrator.myapplication2.Adapter.MyAdapter;
import com.example.administrator.myapplication2.Bean.MyBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class GameReady extends AppCompatActivity {

    private RecyclerView rv;
    private Button btn;
    private Socket socket;
    private MyBean mb;
    private ArrayList<MyBean> list;
    private MyAdapter adapter;
    private Intent intent;
    private int num;
    private int sum;
    private int index;
    private String name;
    private String img;
    private int sign;
    private String[] status = {"房主","未准备","已准备"};
    private OutputStream outputStream;
    private JSONArray JA;
    private JSONObject JO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_ready);

        rv = (RecyclerView) findViewById(R.id.rv);
        btn = (Button) findViewById(R.id.btn);
        list = new ArrayList<>();
        adapter = new MyAdapter(this);
        intent=this.getIntent();
        num = Integer.parseInt(intent.getStringExtra("num"));
        sum = 0;
        name = intent.getStringExtra("name");
        img = "img" + name;
        sign = 0;

        try{
            if(num == 0){
                JA = new JSONArray();
                for(int i = 0;i<3;i++){
                    JO = new JSONObject();
                    JO.put("num",i);
                    JO.put("name"," ");
                    JO.put("img"," ");
                    JO.put("status"," ");
                    JA.put(JO);
                }
            }
        }catch (Exception e){
            Log.e("XXX", "Exception:"+e.getStackTrace().toString());
        }

        final Handler handler = new GameReady.MyHandler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.154.1", 10010);
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    String str;
                    Message message;

                    outputStream = socket.getOutputStream();
                    outputStream.write(( "join//" + num + "//" + name + "//" + img + "//" + status[1]).getBytes("utf-8"));
                    outputStream.flush();

                    while ((len = inputStream.read(buffer)) != -1) {
                        str = new String(buffer, 0, len);
                        if(strToList(str) == 1){
                            outputStream.write(("list//" + JA.toString()).getBytes("utf-8"));
                            outputStream.flush();
                        }

                        // 发到主线程中 收到的数据
                        message = Message.obtain();
                        message.what = 1;
                        message.obj = str;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Log.e("XXX", "Exception:"+e.getStackTrace().toString());
                }
            }
        }).start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num == 0){
                    if(isReady()==true){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    outputStream.write(("start//").getBytes("utf-8"));
                                    outputStream.flush();
                                }catch (Exception e) {
                                    Log.e("XXX", "Exception:"+e.getStackTrace().toString());
                                }
                            }
                        }).start();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(GameReady.this);
                        builder.setTitle("警告框");
                        builder.setMessage("玩家未全部准备！");
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Toast.makeText(GameReady.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                }else{
                    sign = (sign+1)%2;
                    if(sign + 1 == 1)
                        btn.setText("准备");
                    else
                        btn.setText("取消准备");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                outputStream.write((index + "//" + sign).getBytes("utf-8"));
                                outputStream.flush();
                            }catch (Exception e) {
                                Log.e("XXX", "Exception:"+e.getStackTrace().toString());
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private boolean isReady(){
        try{
            int i;
            for(i=0;i<JA.length();i++){
                if((JA.getJSONObject(i).get("status").equals("未准备"))||(JA.getJSONObject(i).get("status").equals(" ")))
                    break;
            }
            if(i==3)
                return true;
            else
                return false;
        }catch (Exception e) {
            Log.e("XXX", "Exception:"+e.getStackTrace().toString());
        }
        return false;
    }

    private int strToList(String str){
        try{
            String[] split = str.split("//");
            if(split[0].equals("join")){
                if(num == 0){
                    int minIndex = 3;
                    int minNum = 3;
                    for(int i=0;i<JA.length();i++){
                        JO = JA.getJSONObject(i);
                        if(JO.get("name").equals(" ")){
                            int n = JO.getInt("num");
                            if(n<minNum){
                                minIndex = i;
                                minNum = n;
                            }
                        }
                    }
                    JO = JA.getJSONObject(minIndex);
                    JO.put("name",split[2]);
                    JO.put("img",split[3]);
                    JO.put("status",split[4]);
                    createList();
                    return 1;
                }
            }else if(split[0].equals("list")){
                if(!(num == 0)){
                    JA = new JSONArray(split[1]);
                    createList();
                }
            }else if(split[0].equals("leave")){
                if(num == 0){
                    int n = JA.getJSONObject(Integer.parseInt(split[1])).getInt("num");
                    JA.getJSONObject(Integer.parseInt(split[1])).put("num",3);
                    JA.getJSONObject(Integer.parseInt(split[1])).put("name"," ");
                    JA.getJSONObject(Integer.parseInt(split[1])).put("img"," ");
                    JA.getJSONObject(Integer.parseInt(split[1])).put("status"," ");

                    for(int i=0;i<JA.length();i++){
                        JO = JA.getJSONObject(i);
                        if(n<=JO.getInt("num")){
                            JO.put("num",JO.getInt("num")-1);
                        }
                        if(JO.getInt("num")==0){
                            JO.put("status",status[0]);
                        }
                    }
                    return 1;
                }
            }else if(split[0].equals("start")){
                Intent intent=new Intent(GameReady.this,GameStart.class);
                intent.putExtra("name",name);
                intent.putExtra("img",img);
                startActivity(intent);
            }else{
                mb = list.get(Integer.parseInt(split[0]));
                String sta = status[Integer.parseInt(split[1])+1];
                mb.setTime(sta);
                JA.getJSONObject(Integer.parseInt(split[0])).put("status",sta);
            }
        }catch (Exception e){
            Log.e("XXX", "Exception:"+e.getStackTrace().toString());
        }
        return 0;
    }
    
    private void createList(){
        try{
            list = new ArrayList<>();
            for(int i = 0;i<JA.length();i++){
                mb = new MyBean(JA.getJSONObject(i).getString("name"),JA.getJSONObject(i).getString("img"),JA.getJSONObject(i).getString("status"),3);
                list.add(mb);
                if(JA.getJSONObject(i).getString("name").equals(name)){
                    num = JA.getJSONObject(i).getInt("num");
                    index = i;
                }
            }
            if(num == 0){
                btn.setText("开始游戏");
                JA.getJSONObject(index).put("status",status[0]);
                list.get(index).setTime(status[0]);
            }
        }catch (Exception e){
            Log.e("XXX", "Exception:"+e.getStackTrace().toString());
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.write(("leave//" + index).getBytes("utf-8"));
                    outputStream.flush();
                } catch (IOException e) {
                    Log.e("XXX", "Exception:"+e.getStackTrace().toString());
                }
            }
        }).start();
    }

    private class MyHandler extends Handler {
        private OutputStream outputStream;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                if (msg.what == 1) {
                    // 向适配器set数据
                    adapter.setData(list);
                    rv.setAdapter(adapter);
                    LinearLayoutManager manager = new LinearLayoutManager(GameReady.this, LinearLayoutManager.VERTICAL, false);
                    rv.setLayoutManager(manager);
                }
            }catch (Exception e){
                Log.e(MainActivity.class.getCanonicalName(), "Exception:"+e.getStackTrace().toString());
            }
        }
    }
}
