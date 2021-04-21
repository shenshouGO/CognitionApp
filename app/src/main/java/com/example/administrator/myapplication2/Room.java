package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication2.Adapter.AnswerAdapter;
import com.example.administrator.myapplication2.Adapter.DoubleScoreAdapter;
import com.example.administrator.myapplication2.Adapter.LeftUserAdapter;
import com.example.administrator.myapplication2.Adapter.ResultScoreAdapter;
import com.example.administrator.myapplication2.Adapter.RightUserAdapter;
import com.example.administrator.myapplication2.Bean.Answer;
import com.example.administrator.myapplication2.Bean.DoubleScore;
import com.example.administrator.myapplication2.Bean.ResultScore;
import com.example.administrator.myapplication2.Bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import MyClass.CustomVideoView;
import MyClass.FileUtils;
import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;

import static android.view.View.VISIBLE;

public class Room extends AppCompatActivity {
    private ListView left_user;
    private ListView right_user;
    private LeftUserAdapter lua;
    private RightUserAdapter rua;
    private AnswerAdapter aa;
    private List<User> users;
    private List<User> left_users;
    private List<User> right_users;
    private List<Answer> answers;
    private JSONArray aaa;
    private  AnswerAdapter a;
    private List<Answer> contents;
    private List<DoubleScore> doubleScore;
    private List<ResultScore> doubleS;
    private int[] dis;
    private List<ResultScore> resultS;
    private User user;
//    private RecyclerView rv;
    private ListView rv;
    private EditText et;
    private Button ready;
    private Button send;
    private int num;
    private int sum;
    private int index;
    private String name;
    private String img;
    private int sign;
    private String[] status = {"房主","未准备","已准备"};
    private Intent intent;
    private Socket socket;
    private OutputStream outputStream;
    private JSONArray JA;
    private JSONObject JO;
    private RelativeLayout material;
    private ImageView iv;
    private CustomVideoView video;
    private MediaController mediaController;
    private TextView text;
    private String path;
    final Handler handler = new MyHandler();
    private FileUtils fu;
    private Boolean gameing;
    private Boolean scoring;
    private LinearLayout edit_frame;
    private TextView time;
    private int cp;
    private JSONArray doubleScores;
    private JSONArray returnScores;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private int returnS = 0;
    private String username;
    private RatingBar validity;
    private RatingBar novelty;
    private int ListNum;
    private Boolean playing;
    private ImageView back;

    private TextView type_text;
    private TextView mode_text;
    private TextView subject_text;
    private String type;
    private String mode;
    private String subject;
    private Random random = new Random();
    String file;

    private HttpUtil httpUtil;
    private Map<String,String> params;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room);

        httpUtil = new HttpUtil();

        left_user = (ListView) findViewById(R.id.left_user);
        right_user = (ListView) findViewById(R.id.right_user);
//        rv = (RecyclerView) findViewById(R.id.rv);
        rv = (ListView) findViewById(R.id.rv);
        et = (EditText) findViewById(R.id.et);
        ready = (Button) findViewById(R.id.ready);
        type_text = (TextView) findViewById(R.id.type);
        mode_text = (TextView) findViewById(R.id.mode);
        subject_text = (TextView) findViewById(R.id.subject);
        send = (Button) findViewById(R.id.send);
        material = (RelativeLayout) findViewById(R.id.material);
        edit_frame = (LinearLayout) findViewById(R.id.edit_frame);
        edit_frame.setVisibility(View.GONE);
        time = (TextView) findViewById(R.id.time);
        time.setVisibility(View.GONE);
        back = (ImageView) findViewById(R.id.back);

        intent=this.getIntent();
        num = intent.getIntExtra("num",1);//获取身份标号，0为房主，1为普通玩家
        name = intent.getStringExtra("name");//获取进入房间的用户名称
        type = intent.getStringExtra("type");//获取该房间的材料类型
        mode = intent.getStringExtra("mode");//获取该房间的材料重评方式
        subject = intent.getStringExtra("subject");//获取该房间的材料主题
        type = "图片";
        mode = "描述";
        subject = "生活";

        type_text.setText("本次重评材料：");
        mode_text.setText("本次重评方式：");
        subject_text.setText("本次材料主题：");

        iv = new ImageView(this);
        video = new CustomVideoView(this);
        text = new TextView(this);

        sign = 0;
        gameing = false;
        scoring = false;
        playing = true;

        left_users = new LinkedList<User>() ;
        right_users = new LinkedList<User>() ;
        answers = new ArrayList<Answer>() ;
        aaa = new JSONArray() ;
        lua = new LeftUserAdapter(Room.this,(List<User>)left_users);
        rua = new RightUserAdapter(Room.this,(List<User>)right_users);
        aa = new AnswerAdapter(Room.this,(List<Answer>)answers);
        a = new AnswerAdapter(Room.this,(List<Answer>)contents);

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
            e.printStackTrace();
        }

        //Android不支持在主线程中进行网络操作，所以新建一个线程进行socket连接
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //建立socket连接
                    socket = new Socket("192.168.154.1", 10010);
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    String str;
                    Message message;

                    final UserInfo UI= (UserInfo)getApplication();

                    //当有用户进行socket连接进入房间时，发送加入房间的消息
                    outputStream = socket.getOutputStream();
                    outputStream.write(( "join//" + num + "//" + name + "//" + UI.getImg() + "//" + status[1]+"!").getBytes("utf-8"));
                    outputStream.flush();

                    //根据当前游戏状态和接收到的消息进行数据更新操作
                    while (playing&&(len = inputStream.read(buffer)) != -1) {
                        str = new String(buffer, 0, len);
                        Log.e("string",str);
                        if(!gameing){
                            if(strToList(str) == 1){
                                outputStream.write(("list//" + JA.toString()+"!").getBytes("utf-8"));
                                outputStream.flush();
                            }

                            message = Message.obtain();
                            message.what = 1;
                            message.obj = str;
                            handler.sendMessage(message);
                        }else{
                            if(scoring){
                                createScore(str);
                            }else{
                                message = Message.obtain();
                                message.what = 3;
                                message.obj = str;
                                handler.sendMessage(message);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num == 0){
                    if(isReady()==true){
                        Random random = new Random();
                        final int n = random.nextInt(3);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    outputStream.write(("start//"+(n+1)+"!").getBytes("utf-8"));
                                    outputStream.flush();
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else{
                        Toast.makeText(Room.this,"玩家未全部准备！",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    sign = (sign+1)%2;
                    if(sign + 1 == 1)
                        ready.setText("准备");
                    else
                        ready.setText("取消准备");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                outputStream.write(("status//"+index + "//" + sign+"!").getBytes("utf-8"));
                                outputStream.flush();
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String data = et.getText().toString();
                et.setText("");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write((name + "//" + data+"!").getBytes("utf-8"));
                            outputStream.flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createScore(String str){
        try{
            String[] split = str.split("//");
            if(split[0].equals("doubleScore")){
                if(index != cp){
                    JO = new JSONObject(split[1]);
                    doubleScore.add(new DoubleScore(JO.getString("name"),JO.getString("validity"),JO.getString("novelty"),JO.getString("average")));
                }
            }else if(split[0].equals("returnScore")){
                JO = new JSONObject();
                JO.put("name",split[1]);
                JO.put("score",split[2]);
                returnS += Integer.parseInt(split[2]);
                returnScores.put(JO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isReady(){
        try{
            int i;
            String s;
            for(i=0;i<users.size();i++){
                s = users.get(i).getStatus();
                if((s.equals("未准备"))||(s.equals(" ")))
                    break;
            }
            if(i==users.size())
                return true;
            else
                return false;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //处理接收到的socket消息
    private  int strToList(String str){
        try{
            String[] splits = str.split("!");//防止多条消息合并遗漏数据，用'!'分隔逐一处理
            for (int k = 0;k<splits.length;k++){
                String[] split = splits[k].split("//");
                if(split[0].equals("join")){//接收到用户加入房间的消息
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
                        JO = JA.getJSONObject(minIndex);//更新加入用户的信息
                        JO.put("name",split[2]);
                        JO.put("img",split[3]);
                        JO.put("status",split[4]);

                        params = new HashMap<String,String>();
                        params.put("sql","select file from cognition_resource where type = '" + type +"' and theme = '"+subject+"'");
                        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/displaySql.do",params,new MyStringCallBack() {
                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JO = new JSONObject(response);
                                    JO = JO.getJSONObject(random.nextInt(JO.length())+"");
//                                    file = JO.getString("file");
                                    file = "毒蛇攻击.png";

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {//由房主将游戏材料的相关信息发出
                                                outputStream.write(("resource//"+type+"/"+mode+"/"+subject+"/"+file+"!").getBytes("utf-8"));
                                                outputStream.flush();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        createList();
                        return 1;
                    }
                }else if(split[0].equals("list")){//接收到更新用户列表信息的消息
                    if(!(num == 0)){
                        JA = new JSONArray(split[1]);
                        createList();
                    }
                }else if(split[0].equals("leave")){//接收到用户离开房间的消息
                    if(num == 0){
                        int n = JA.getJSONObject(Integer.parseInt(split[1])).getInt("num");
                        JO = JA.getJSONObject(Integer.parseInt(split[1]));
                        JO.put("num",3);
                        JO.put("name"," ");
                        JO.put("img"," ");
                        JO.put("status"," ");
                        user = users.get(Integer.parseInt(split[1]));
                        user.setNum(3);
                        user.setName(" ");
                        user.setImg(" ");
                        user.setStatus(" ");

                        if(n == 0) {
                            for (int i = 0; i < JA.length(); i++) {
                                JO = JA.getJSONObject(i);
                                int j = JO.getInt("num");
                                JO.put("num", j - 1);
                                if(j == 1)
                                    JO.put("status",status[0]);
                            }
                        }else{
                            for (int i = 0; i < JA.length(); i++) {
                                JO = JA.getJSONObject(i);
                                int j = JO.getInt("num");
                                if (n <= j) {
                                    JO.put("num", j - 1);
                                }
                            }
                        }
                        return 1;
                    }
                }else if(split[0].equals("start")){//接收到游戏开始的消息
                    gameing = true;

                    Message message = Message.obtain();
                    message.what = 2;
                    message.obj = split[1];
                    handler.sendMessage(message);

                }else if(split[0].equals("status")){//接收到用户改变准备状态的消息
                    user = users.get(Integer.parseInt(split[1]));
                    String sta = status[Integer.parseInt(split[2])+1];
                    user.setStatus(sta);
                    JA.getJSONObject(Integer.parseInt(split[1])).put("status",sta);
                }else if(split[0].equals("resource")){//接收到更新游戏材料相关信息的消息
                    split = split[1].split("/");
                    type = split[0];
                    mode = split[1];
                    subject = split[2];
                    file = split[3];
                    for (int i = 0;i<split.length;i++){
                        Log.e("resource",split[i]);
                    }

                    Message message = Message.obtain();
                    message.what = 8;
                    handler.sendMessage(message);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private void createList(){
        try{
            users = new LinkedList<User>() ;
            for(int i = 0;i<JA.length();i++){
                user = new User(JA.getJSONObject(i).getString("name"),JA.getJSONObject(i).getString("img"),JA.getJSONObject(i).getInt("num"),JA.getJSONObject(i).getString("status"));
                users.add(user);
                if(JA.getJSONObject(i).getString("name").equals(name)){
                    num = JA.getJSONObject(i).getInt("num");
                    index = i;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    @Override
//    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    outputStream.write(("leave//" + index).getBytes("utf-8"));
//                    outputStream.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    @Override
    public void finish() {
        super.finish();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    playing = false;
                    outputStream.write(("leave//" + index+"!").getBytes("utf-8"));
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                if (msg.what == 1) {
                    if(num == 0){
                        ready.setText("开始游戏");
                        JA.getJSONObject(index).put("status",status[0]);
                        users.get(index).setStatus(status[0]);
                    }

                    left_users = users.subList(0,2);
                    right_users = users.subList(2,3);
                    lua.setData(left_users);
                    rua.setData(right_users);
                    left_user.setAdapter(lua);
                    right_user.setAdapter(rua);
                }else if(msg.what == 2){
                    ready.setVisibility(View.GONE);
                    cp = Integer.parseInt((String)msg.obj);
                    Toast.makeText(Room.this,cp+"号为裁判，其余为玩家！",Toast.LENGTH_SHORT).show();
                    cp--;
                    if(index != cp)
                        edit_frame.setVisibility(VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                File f = new File(path, "maidang.jpg");
//                                Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f));
//                                iv.setImageBitmap(bmp);
                                switch (type){
                                    case "视频":
                                        material.addView(video);
                                        break;
                                    case "图片":
                                        material.addView(iv);
                                        break;
                                    case "文本":
                                        material.addView(text);
                                        break;
                                }

                                Toast.makeText(Room.this,"开始游戏，请玩家在75s内进行重评!",Toast.LENGTH_SHORT).show();
                                countDown(10*1000,4);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }, 1000);
                }else if(msg.what == 3){
                    String[] split = ((String) msg.obj).split("//");
                    answers.add(new Answer(split[0],split[1]));
                    JO = new JSONObject();
                    JO.put("name",split[0]);
                    JO.put("answer",split[1]);
                    aaa.put(JO);
//                    aa.setData(answers);
//                    rv.setAdapter(aa);
//                    rv.setSelection(aa.getCount()-1);
                }else if(msg.what == 4){
                    edit_frame.setVisibility(View.GONE);
                    Toast.makeText(Room.this,"请裁判在60s内进行二维评分!",Toast.LENGTH_SHORT).show();
                    scoring = true;
                    countDown(10*1000,5);
                    downKeyboard();

                    aa.setData(answers);
                    rv.setAdapter(aa);

                    doubleScores = new JSONArray();
                    dis = new int[3];
                    JO = new JSONObject();
                    JO.put("name","玩家名称");
                    JO.put("validity","有效性");
                    JO.put("novelty","新颖性");
                    JO.put("average","平均分");
                    doubleScores.put(JO);
                    doubleScore = new LinkedList<DoubleScore>();
                    doubleScore.add(new DoubleScore("玩家名称","有效性","新颖性","平均分"));
                    if(index == cp){
                        doubleScore(0);
                    }
                }else if(msg.what == 5){
                    Toast.makeText(Room.this,"请玩家在60s内对裁判进行反评!",Toast.LENGTH_SHORT).show();
                    countDown(10*1000,6);
                    returnScores = new JSONArray();
                    if(index != cp){
                        returnScore();
                    }
                }else if(msg.what == 6){
                    Toast.makeText(Room.this,"游戏结束!",Toast.LENGTH_SHORT).show();
                    time.setVisibility(View.GONE);
                    resultScore();

                    sign = 0;
                    returnS = 0;
                    gameing = false;
                    scoring = false;
                    material.removeView(iv);
                    aa.clear();
                    aaa = new JSONArray() ;
                    rv.setAdapter(null);

                    for(int i =0;i<users.size();i++){
                        if(users.get(i).getNum() != 0){
                            users.get(i).setStatus(status[1]);
                            JA.getJSONObject(i).put("status",status[1]);
                        }
                    }
                    Message ms = new Message();
                    ms.what = 1;
                    handler.sendMessage(ms);
                    ready.setText("准备");
                    ready.setVisibility(VISIBLE);
                }if(msg.what == 7){
                    final String s = (String)msg.obj+"!";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                outputStream.write(s.getBytes("utf-8"));
                                outputStream.flush();
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else if(msg.what == 8){
                    //更新材料
                    type_text.setText("本次重评材料："+type);
                    mode_text.setText("本次重评方式："+mode);
                    subject_text.setText("本次材料主题："+subject);

                    switch (type){
                        case "视频":
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
//                            videoUrl = "http://192.168.154.1:8080/file/你的答案.mp4";
                            videoUrl = "http://192.168.154.1:8080/file/"+file;
                            Bitmap bitmap = null;
                            //video.setVideoURI(Uri.parse("http://192.168.154.1:8080/file/2019跨年.mp4" ));
                            video.setVideoPath(videoUrl);
                            //设置视频缩略图
                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(videoUrl, new HashMap());
                            bitmap = retriever.getFrameAtTime();
                            video.setBackground(new BitmapDrawable(getResources(),bitmap));
                            mediaController = new MediaController(mContext);
                            video.setMediaController(mediaController);
                            mediaController.setMediaPlayer(video);

                            material.addView(video);
                            break;
                        case "图片":
                            iv.setVisibility(VISIBLE);
                            path = "http://192.168.154.1:8080/file/"+file;
//                            fu = new FileUtils();
//                            fu.downLoad(path,"maidang.jpg");
//                            path = Environment.getExternalStorageDirectory().toString() + "/shidoe";
//                            File f = new File(path, "maidang.jpg");
//                            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f));
//                            iv.setImageBitmap(bmp);

                            Glide.with(mContext).load(path).into(iv);

                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent = new Intent(Room.this,ImageDetail.class);
//                                    intent.putExtra("path",path);
//                                    intent.putExtra("img","maidang.jpg");
                                    intent.putExtra("path","http://192.168.154.1:8080/file/");
                                    intent.putExtra("img",file);
                                    startActivity(intent);
                                }
                            });
                            material.addView(iv);
                            break;
                        case "文本":
                            text.setVisibility(VISIBLE);

                            params = new HashMap<String, String>();
//                            params.put("file","文本材料1.txt");
                            params.put("file",file);
                            httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
                                @Override
                                public void onResponse(String response, int id) {
                                    text.setText(response);
                                }
                            });
                            material.addView(text);
                            break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private  void doubleScore(int i){
        final LayoutInflater inflater = Room.this.getLayoutInflater();
        View ds = inflater.inflate(R.layout.double_score, null,false);
        TextView name = ds.findViewById(R.id.name);
        ListView content = ds.findViewById(R.id.content);
        validity = ds.findViewById(R.id.validity);
        novelty = ds.findViewById(R.id.novelty);
        Button OK = ds.findViewById(R.id.OK);
        if(i == cp)
            ListNum = ++i;
        else
            ListNum = i;

        username = users.get(i).getName();
        name.setText(username);
        contents = new ArrayList<Answer>() ;
        try{
            for(int j = 0;j<aaa.length();j++){
                JO = aaa.getJSONObject(j);
                String s = JO.getString("name");
                if (username.equals(s))
                    contents.add(new Answer(s,JO.getString("answer")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        a.setData(contents);
        content.setAdapter(a);

        builder = new AlertDialog.Builder(this);
        builder.setView(ds);
        builder.setCancelable(false);
        if((ViewGroup)ds.getParent() != null)
        {
            ((ViewGroup)ds.getParent()).removeView(ds);
        }
        alert = builder.create();
        alert.show();

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JO = new JSONObject();
                    JO.put("name",username);
                    JO.put("validity",""+(int)validity.getRating());
                    JO.put("novelty",""+(int)novelty.getRating());
                    JO.put("average",String.format("%.1f",(validity.getRating()+novelty.getRating())/2));
                    doubleScore.add(new DoubleScore(JO.getString("name"),JO.getString("validity"),JO.getString("novelty"),JO.getString("average")));

                    Message ms = new Message();
                    ms.what = 7;
                    ms.obj = "doubleScore//"+JO.toString();
                    handler.sendMessage(ms);

                    alert.dismiss();

                    if(doubleScore.size()<JA.length()){
                        doubleScore(ListNum+1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void returnScore(){
        final LayoutInflater inflater = Room.this.getLayoutInflater();
        View rs = inflater.inflate(R.layout.return_score, null,false);
        ListView content = rs.findViewById(R.id.content);
        final RatingBar fairness = rs.findViewById(R.id.fairness);
        Button OK = rs.findViewById(R.id.OK);

        DoubleScoreAdapter dsa = new DoubleScoreAdapter(Room.this,doubleScore);
        content.setAdapter(dsa);

        builder = new AlertDialog.Builder(this);
        builder.setView(rs);
        builder.setCancelable(false);
        if((ViewGroup)rs.getParent() != null)
        {
            ((ViewGroup)rs.getParent()).removeView(rs);
        }
        alert = builder.create();
        alert.show();

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message message = new Message();
                    message.what = 7;
                    message.obj = "returnScore//"+name+"//"+(int)fairness.getRating();
                    handler.sendMessage(message);
                    alert.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void resultScore(){
        final LayoutInflater inflater = Room.this.getLayoutInflater();
        View gr = inflater.inflate(R.layout.game_result, null,false);
        ListView scores = gr.findViewById(R.id.scores);
        ListView fairness = gr.findViewById(R.id.fairness);
        Button OK = gr.findViewById(R.id.OK);

        doubleS = new LinkedList<ResultScore>();
        resultS = new LinkedList<ResultScore>();
        ResultScore rs;
        DoubleScore ds;

        try{
            bulidDis();
            doubleS.add(new ResultScore("玩家名称","有效性","新颖性","平均分","积分"));
            for(int i = 1;i<doubleScore.size();i++){
                ds = doubleScore.get(i);
                doubleS.add(new ResultScore(ds.getName(),ds.getValidity(),ds.getNovelty(),ds.getAverage(),"+"+dis[i]));
                if(num == 0){
                    params = new HashMap<String, String>();
                    params.put("name",ds.getName());
                    params.put("integral",""+dis[i]);
                    httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/createGameIntegral.do",params,new MyStringCallBack(){
                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("response:",response);
                        }
                    });
                }
            }

            resultS.add(new ResultScore("被评玩家","评价玩家","公正性","平均分","积分"));
            String cpName = JA.getJSONObject(cp).getString("name");
            double s = returnS*1.0/returnScores.length();
            String a = String.format("%.1f",s);
            int ri = 0;
            if(s>=6)
                ri = 5;
            else if(s>=4)
                ri = 3;
            for(int i = 0;i<returnScores.length();i++){
                JO = returnScores.getJSONObject(i);
                resultS.add(new ResultScore(cpName,JO.getString("name"),JO.getString("score"),a,"+"+ri));
            }
            if(num == 0){
                params = new HashMap<String, String>();
                params.put("name",cpName);
                params.put("integral",""+ri);
                httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/createGameIntegral.do",params,new MyStringCallBack(){
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("response:",response);
                    }
                });
            }

            ResultScoreAdapter rsa1 = new ResultScoreAdapter(Room.this,doubleS);
            ResultScoreAdapter rsa2 = new ResultScoreAdapter(Room.this,resultS);
            scores.setAdapter(rsa1);
            fairness.setAdapter(rsa2);

            builder = new AlertDialog.Builder(this);
            builder.setView(gr);
            builder.setCancelable(false);
            if((ViewGroup)gr.getParent() != null)
            {
                ((ViewGroup)gr.getParent()).removeView(gr);
            }
            alert = builder.create();
            alert.show();

            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void bulidDis(){
        DoubleComparator dc = new DoubleComparator();
        Collections.sort(doubleScore,dc);
        double d;

        dis[1]+=5;
        for(int i = 1;i<doubleScore.size();i++){
            d=Double.parseDouble(doubleScore.get(i).getAverage());
            if(d>=5)
                dis[i]+=5;
        }
    }

    class DoubleComparator implements Comparator{
        public int compare(Object arg0, Object arg1) {
            DoubleScore d0=(DoubleScore)arg0;
            DoubleScore d1=(DoubleScore)arg1;

            return d1.getAverage().compareTo(d0.getAverage());
        }
    }

    private void tip(String tip){
        AlertDialog.Builder builder = new AlertDialog.Builder(Room.this);
        final AlertDialog alert = builder.setTitle("系统提示：")
                .setMessage(tip)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        alert.show();
    }

    private void countDown(long countTime, final int num){
        time.setVisibility(VISIBLE);
        CountDownTimer timer = new CountDownTimer(countTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(""+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                if(alert!=null)
                    if(alert.isShowing())
                        alert.dismiss();
                Message message = Message.obtain();
                message.what = num;
                handler.sendMessage(message);
            }
        };
        timer.start();
    }

    private void downKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}
