package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class Room extends AppCompatActivity {
    private ListView left_user;
    private ListView right_user;
    private LeftUserAdapter lua;
    private RightUserAdapter rua;
    private  AnswerAdapter aa;
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
    private int img;
    private int sign;
    private String[] status = {"房主","未准备","已准备"};
    private Intent intent;
    private Socket socket;
    private OutputStream outputStream;
    private JSONArray JA;
    private JSONObject JO;
    private RelativeLayout material;
    private ImageView iv;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room);

        left_user = (ListView) findViewById(R.id.left_user);
        right_user = (ListView) findViewById(R.id.right_user);
//        rv = (RecyclerView) findViewById(R.id.rv);
        rv = (ListView) findViewById(R.id.rv);
        et = (EditText) findViewById(R.id.et);
        ready = (Button) findViewById(R.id.ready);
        send = (Button) findViewById(R.id.send);
        material = (RelativeLayout) findViewById(R.id.material);
        edit_frame = (LinearLayout) findViewById(R.id.edit_frame);
        edit_frame.setVisibility(View.GONE);
        time = (TextView) findViewById(R.id.time);
        time.setVisibility(View.GONE);

        intent=this.getIntent();
        num = intent.getIntExtra("num",1);
//        num = 0;
        name = intent.getStringExtra("name");
//        name = "user1";
        img = R.drawable.ic_launcher_round;
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
                    JO.put("img",R.drawable.p);
                    JO.put("status"," ");
                    JA.put(JO);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

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

                    while (playing&&(len = inputStream.read(buffer)) != -1) {
                        str = new String(buffer, 0, len);
                        Log.e("string",str);
                        if(!gameing){
                            if(strToList(str) == 1){
                                outputStream.write(("list//" + JA.toString()).getBytes("utf-8"));
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
                        Random r = new Random();
                        final int n = r.nextInt(3);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    outputStream.write(("start//"+(n+1)).getBytes("utf-8"));
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
                                outputStream.write(("status//"+index + "//" + sign).getBytes("utf-8"));
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
                            outputStream.write((name + "//" + data).getBytes("utf-8"));
                            outputStream.flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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

    private  int strToList(String str){
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
                    JO = JA.getJSONObject(Integer.parseInt(split[1]));
                    JO.put("num",3);
                    JO.put("name"," ");
                    JO.put("img",R.drawable.p);
                    JO.put("status"," ");
                    user = users.get(Integer.parseInt(split[1]));
                    user.setNum(3);
                    user.setName(" ");
                    user.setImg(R.drawable.p);
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
            }else if(split[0].equals("start")){
//                for(int i= 0;i<users.size();i++)
//                {
//                    users.get(i).setStatus(" ");
//                }

                iv = new ImageView(this);
                iv.setVisibility(View.VISIBLE);
                path = "http://47.95.197.189:8080/file/1.jpg";

                fu = new FileUtils();
                fu.downLoad(path,"maidang.jpg");
                path = Environment.getExternalStorageDirectory().toString() + "/shidoe";
                gameing = true;

                Message message = Message.obtain();
                message.what = 2;
                message.obj = split[1];
                handler.sendMessage(message);

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(Room.this,ImageDetail.class);
                        intent.putExtra("path",path);
                        intent.putExtra("img","maidang.jpg");
                        startActivity(intent);
                    }
                });
            }else if(split[0].equals("status")){
                user = users.get(Integer.parseInt(split[1]));
                String sta = status[Integer.parseInt(split[2])+1];
                user.setStatus(sta);
                JA.getJSONObject(Integer.parseInt(split[1])).put("status",sta);
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
                user = new User(JA.getJSONObject(i).getString("name"),JA.getJSONObject(i).getInt("img"),JA.getJSONObject(i).getInt("num"),JA.getJSONObject(i).getString("status"));
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
                    outputStream.write(("leave//" + index).getBytes("utf-8"));
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
                    if(index != cp)
                        edit_frame.setVisibility(View.VISIBLE);
                    cp = Integer.parseInt((String)msg.obj);
                    Toast.makeText(Room.this,cp+"号为裁判，其余为玩家！",Toast.LENGTH_SHORT).show();
                    cp--;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                File f = new File(path, "maidang.jpg");
                                Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f));
                                iv.setImageBitmap(bmp);
                                material.addView(iv);

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
                    ready.setVisibility(View.VISIBLE);
                }if(msg.what == 7){
                    final String s = (String)msg.obj;
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
            }

            resultS.add(new ResultScore("被评玩家","评价玩家","公正性","平均分","积分"));
            String cpName = JA.getJSONObject(cp).getString("name");
            double s = returnS*1.0/returnScores.length();
            String a = String.format("%.1f",s);
            String ri;
            if(s>=5)
                ri = "+5";
            else
                ri = "+0";
            for(int i = 0;i<returnScores.length();i++){
                JO = returnScores.getJSONObject(i);
                resultS.add(new ResultScore(cpName,JO.getString("name"),JO.getString("score"),a,ri));
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
        time.setVisibility(View.VISIBLE);
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
