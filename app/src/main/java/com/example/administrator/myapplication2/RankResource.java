package com.example.administrator.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication2.Adapter.OtherCognitionsAdapter;
import com.example.administrator.myapplication2.Bean.OtherCognitions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import MyClass.CustomVideoView;
import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;

public class RankResource extends AppCompatActivity implements View.OnClickListener{
    private HttpUtil httpUtil;
    private Map<String,String> params;
    final Handler handler = new MyHandler();
    private Intent intent;
    private JSONObject info;
    private JSONObject results;
    private List<OtherCognitions> resources;
    private OtherCognitionsAdapter oca;
    private JSONObject JO;
    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private ImageView back;
    private TextView title;
    private TextView rank;
    private ImageView pic;
    private TextView text;
    private CustomVideoView video;
    private MediaController mediaController;
    private RelativeLayout detail;
    private ImageView img;
    private TextView name;
    private TextView cognition;
    private TextView time;
    private TextView comment;
    private TextView good;
    private ListView others;
    private LinearLayout edit_frame;
    private EditText edit;
    private Button send;

    private String file;
    private String[] splits;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_resource);

        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        rank = (TextView)findViewById(R.id.rank);
        pic = (ImageView)findViewById(R.id.pic);
        text = (TextView)findViewById(R.id.text);
        video = (CustomVideoView)findViewById(R.id.video);
        detail = (RelativeLayout) findViewById(R.id.detail);
        img = (ImageView)findViewById(R.id.img);
        name = (TextView)findViewById(R.id.name);
        cognition = (TextView)findViewById(R.id.cognition);
        time = (TextView)findViewById(R.id.time);
        comment = (TextView)findViewById(R.id.comment);
        good = (TextView)findViewById(R.id.good);
        others = (ListView)findViewById(R.id.others);
        edit_frame = (LinearLayout) findViewById(R.id.edit_frame);
        edit = (EditText) findViewById(R.id.edit);
        send = (Button) findViewById(R.id.send);
        resources = new LinkedList<OtherCognitions>();

        back.setOnClickListener(this);
        cognition.setOnClickListener(this);
        send.setOnClickListener(this);

        httpUtil = new HttpUtil();

        intent = this.getIntent();
        try{
            info = new JSONObject(intent.getStringExtra("info"));
            type = intent.getStringExtra("type");
            title.setText(type);
            rank.setText(intent.getStringExtra("rank"));

            if(type.equals("材料榜单")){
                detail.setVisibility(View.GONE);
                file = info.getString("file");
            } else{
                name.setText(info.getString("u_name"));
                params = new HashMap<String, String>();
                params.put("file",info.getString("file"));
                httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        cognition.setText(response);
                    }
                });
                time.setText(simpleDateFormat.format(new Date(new Long(info.getString("time")))));
                good.setText(info.getString("good")+"点赞");
                comment.setText(info.getString("comment")+"评论");
                file = info.getString("c_r_file");
            }

            Log.e("file",file);
            splits = file.split("\\.");
            if(splits[1].equals("txt")){
                text.setVisibility(View.VISIBLE);
                text.setMovementMethod(ScrollingMovementMethod.getInstance());;

                params = new HashMap<String, String>();
                params.put("file",file);
                httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        splits = response.split("\\|\\|\\|");
                        text.setText(splits[0]);
                    }
                });
            }else if(splits[1].equals("mp4")){
                video.setVisibility(View.VISIBLE);
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
                videoUrl = "http://59.110.215.154:8080/resource/"+file;
                Bitmap bitmap = null;
                //video.setVideoURI(Uri.parse("http://59.110.215.154:8080/resource/2019跨年.mp4" ));
                video.setVideoPath(videoUrl);
                //设置视频缩略图
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(videoUrl, new HashMap());
                bitmap = retriever.getFrameAtTime();
                video.setBackground(new BitmapDrawable(getResources(),bitmap));
                mediaController = new MediaController(this);
                video.setMediaController(mediaController);
                mediaController.setMediaPlayer(video);
            }else{
                pic.setVisibility(View.VISIBLE);
                Glide.with(RankResource.this).load("http://59.110.215.154:8080/resource/"+file).into(pic);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        displayCognition();

        others.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                try{
                    intent = new Intent(RankResource.this,CognitionDetail.class);
                    intent.putExtra("info", results.getJSONObject(""+i).toString());
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        displayCognition();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.cognition:
                intent = new Intent(RankResource.this,CognitionDetail.class);
                intent.putExtra("info", info.toString());
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.send:
                if(TextUtils.isEmpty(edit.getText())){
                    Toast.makeText(RankResource.this,"请输入内容再发送！",Toast.LENGTH_SHORT).show();
                }else{
                    String data = edit.getText().toString();
                    edit.setText("");

                    final UserInfo UI = (UserInfo)getApplication();
                    try {
                        params = new HashMap<String, String>();
                        params.put("ID",UI.getId());
                        params.put("u_name",UI.getName());
                        params.put("u_img",UI.getImg());
                        if(type.equals("材料榜单")){
                            params.put("c_r_id",info.getString("id"));
                            params.put("c_r_file",info.getString("file"));
                        } else{
                            params.put("c_r_id",info.getString("c_r_id"));
                            params.put("c_r_file",info.getString("c_r_file"));
                        }
                        params.put("cognition",data);

                        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/createCognition.do",params,new MyStringCallBack() {
                            @Override
                            public void onResponse(String response, int id) {
                                if(response.equals("Create successfully")){
                                    Toast.makeText(RankResource.this,"重评成功！",Toast.LENGTH_SHORT).show();
                                    displayCognition();
                                    Message message = Message.obtain();
                                    message.what = 2;
                                    handler.sendMessage(message);
                                }else{
                                    Toast.makeText(RankResource.this,"重评失败！",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                }
        }
    }

    private void displayCognition(){
        params = new HashMap<String,String>();
        try {
            if(type.equals("材料榜单"))
                params.put("sql","select * from cognition_result where c_r_id = "+info.getString("id"));
            else
                params.put("sql","select * from cognition_result where id !="+info.getString("id")+" and c_r_id = "+info.getString("c_r_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/displaySql.do",params,new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                Log.e("response:",response+"");
                if(!response.equals("{}")){
                    Message message = new Message();
                    message.what = 1;
                    message.obj = response;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 1://更新重评区
                        results = new JSONObject(msg.obj.toString());
                        resources = new LinkedList<OtherCognitions>() ;
                        for(int i = 0;i<results.length();i++){
                            JO = results.getJSONObject(""+i);
                            date = new Date(new Long(JO.getString("time")));
                            resources.add(new OtherCognitions(JO.getString("id"),JO.getString("u_id"),JO.getString("u_img"),JO.getString("u_name"),JO.getString("file"),simpleDateFormat.format(date),JO.getString("good"),JO.getString("comment"),"1"));
                        }
                        oca = new OtherCognitionsAdapter(RankResource.this,resources,0);
                        others.setAdapter(oca);
                        break;
                    case 2://收起编辑区和输入法
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(imm != null)
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        break;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //隐藏输入框
    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if(v!=null&&isShouldHideInput(ev)){
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessage(message);
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(MotionEvent pEv) {
        boolean isHidden = false;
        int[] leftTop = {0, 0};
        edit_frame.getLocationOnScreen(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + edit_frame.getHeight();
        int right = left + edit_frame.getWidth();
        float locationX = pEv.getRawX();
        float locationY = pEv.getRawY();
        if (locationX > left && locationX < right
                && locationY > top && locationY < bottom) {
            // 点击的是输入框区域，保留点击EditText事件
            isHidden = false;
        } else {
            // 失去焦点
            edit_frame.clearFocus();
            isHidden = true;
        }
        return isHidden;
    }
}
