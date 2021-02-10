package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication2.Adapter.CommentAdapter;
import com.example.administrator.myapplication2.Bean.Comment;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import MyClass.InternetRequest;

public class CognitionDetail extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private ImageView img;
    private TextView name;
    private ImageView pic;
    private TextView text;
    private TextView time;
    private ImageView discuss;
    private ImageView good;
    private ImageView share;
    private Button score;
    private ListView comment;
    private List<Comment> comments;
    private CommentAdapter ca;

    private RatingBar validity;
    private RatingBar novelty;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private Intent intent;
    private JSONObject info;
    private InternetRequest IR;
    private JSONObject results;
    private JSONObject replys;
    private JSONObject JO;
    private String str;
    final Handler handler = new MyHandler();
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cognition_detail);

        back = (ImageView) findViewById(R.id.back);
        img = (ImageView) findViewById(R.id.img);
        name = (TextView) findViewById(R.id.name);
        pic = (ImageView) findViewById(R.id.pic);
        text = (TextView) findViewById(R.id.text);
        time =(TextView) findViewById(R.id.time);
        discuss = (ImageView) findViewById(R.id.discuss);
        good = (ImageView) findViewById(R.id.good);
        share = (ImageView) findViewById(R.id.share);
        score = (Button) findViewById(R.id.score);
        comment = (ListView) findViewById(R.id.comment);
        comments = new LinkedList<Comment>() ;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        intent = this.getIntent();
        try{
            info = new JSONObject(intent.getStringExtra("info"));
            img.setOnClickListener(this);
            name.setText(info.getString("u_name"));
            name.setOnClickListener(this);
            pic.setVisibility(View.GONE);
            text.setText(info.getString("file"));
            time.setText(simpleDateFormat.format(new Date(new Long(info.getString("time")))));
            discuss.setOnClickListener(this);
            good.setOnClickListener(this);
            share.setOnClickListener(this);
            score.setOnClickListener(this);

            IR = new InternetRequest();
            IR.addPara("ID",info.getString("id"));
        }catch (Exception e){
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    str = IR.requestPost("http://47.95.197.189:8080/CognitionAPP/displayComment.do");
                    results = new JSONObject(str);
                    for(int i = 0;i<results.length();i++){
                        JO = results.getJSONObject(""+i);
                        comments.add(new Comment(JO));
                        replys = JO.getJSONObject("replys");
                        for(int j=0;j<replys.length();j++){
                            comments.add(new Comment(replys.getJSONObject(""+j)));
                        }
                    }

                    message = Message.obtain();
                    message.what = 1;
//                    message.obj = str;
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v){
        try{
            switch (v.getId()) {
                case R.id.score:
                    final LayoutInflater inflater = CognitionDetail.this.getLayoutInflater();
                    View ds = inflater.inflate(R.layout.double_score, null,false);
                    TextView name = ds.findViewById(R.id.name);
                    name.setVisibility(View.GONE);
                    ListView content = ds.findViewById(R.id.content);
                    content.setVisibility(View.GONE);
                    validity = ds.findViewById(R.id.validity);
                    novelty = ds.findViewById(R.id.novelty);
                    Button OK = ds.findViewById(R.id.OK);
                    OK.setOnClickListener(this);

                    builder = new AlertDialog.Builder(CognitionDetail.this);
                    builder.setView(ds);
                    builder.setCancelable(false);
                    if((ViewGroup)ds.getParent() != null)
                    {
                        ((ViewGroup)ds.getParent()).removeView(ds);
                    }
                    alert = builder.create();
                    alert.show();
                    break;
                case R.id.OK:
                    Toast.makeText(CognitionDetail.this,"有效性："+(int)validity.getRating()+" 创新性："+(int)novelty.getRating(),Toast.LENGTH_SHORT).show();
                    alert.dismiss();
                    break;
                case R.id.discuss:
                    Toast.makeText(CognitionDetail.this,"评论",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.good:
                    Toast.makeText(CognitionDetail.this,"点赞",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.share:
                    Toast.makeText(CognitionDetail.this,"分享",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.img:
                    Toast.makeText(CognitionDetail.this,"用户"+info.getString("u_name")+"的头像",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.name:
                    Toast.makeText(CognitionDetail.this,"用户"+info.getString("u_name")+"的昵称",Toast.LENGTH_SHORT).show();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    ca = new CommentAdapter(CognitionDetail.this,comments);
                    comment.setAdapter(ca);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
