package com.example.administrator.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.administrator.myapplication2.Adapter.CommentAdapter;
import com.example.administrator.myapplication2.Bean.Comment;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import MyClass.CustomVideoView;
import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;

public class TextDetail extends AppCompatActivity implements View.OnClickListener{
    private CustomVideoView video;
    private MediaController mediaController;
    private TextView text;
    private ListView list;
    private TextView good;
    private TextView unlike;
    private TextView comment;
    private List<Comment> comments;
    private CommentAdapter ca;
    private boolean isComment;
    private String s_c_id;
    private String r_id;
    private String r_name;
    private String r_img;
    private Button delete;
    private JSONObject results;
    private JSONObject replys;
    private JSONObject JO;
    private TextView collect;
    private TextView share;
    private RelativeLayout edit_frame;
    private EditText edit;
    private Button send;
    final Handler handler = new MyHandler();
    private Message message;
    private Intent intent;
    private JSONObject info;
    private HttpUtil httpUtil;
    private Map<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_detail);
        final UserInfo UI = (UserInfo)getApplication();
        //输入框覆盖界面不顶起布局
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        httpUtil = new HttpUtil();
        video = (CustomVideoView)findViewById(R.id.video);
        text = (TextView) findViewById(R.id.text);
        list = (ListView) findViewById(R.id.list);
        good = (TextView) findViewById(R.id.good);
        unlike = (TextView) findViewById(R.id.unlike);
        comment = (TextView) findViewById(R.id.comment);
        comments = new LinkedList<Comment>() ;
        delete = (Button) findViewById(R.id.delete);
        collect = (TextView) findViewById(R.id.collect);
        share = (TextView) findViewById(R.id.share);
        edit_frame = (RelativeLayout) findViewById(R.id.edit_frame);
        edit_frame.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        edit = (EditText) findViewById(R.id.edit);
        send = (Button) findViewById(R.id.send);
        intent = getIntent();
        try {
            info = new JSONObject(intent.getStringExtra("info"));

            if(info.getString("type").equals("视频")){
                text.setVisibility(View.GONE);
                setVideo();
            }else{
                video.setVisibility(View.GONE);
                params = new HashMap<String, String>();
                params.put("file",info.getString("file"));
                httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        super.onResponse(response, id);
                        text.setText(response);
                    }
                });
            }
            displayComments();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Drawable[] goodD = good.getCompoundDrawables();
        Drawable[] unlikeD = unlike.getCompoundDrawables();
        Drawable[] commentD = comment.getCompoundDrawables();
        Drawable[] collectD = collect.getCompoundDrawables();
        Drawable[] shareD = share.getCompoundDrawables();
        goodD[1].setBounds(0,0,50,45);
        unlikeD[1].setBounds(0,0,50,50);
        commentD[1].setBounds(0,10,50,50);
        collectD[1].setBounds(0,0,50,50);
        shareD[1].setBounds(0,0,50,50);
        good.setCompoundDrawables(goodD[0],goodD[1],goodD[2],goodD[3]);
        unlike.setCompoundDrawables(unlikeD[0],unlikeD[1],unlikeD[2],unlikeD[3]);
        comment.setCompoundDrawables(commentD[0],commentD[1],commentD[2],commentD[3]);
        collect.setCompoundDrawables(collectD[0],collectD[1],collectD[2],collectD[3]);
        share.setCompoundDrawables(shareD[0],shareD[1],shareD[2],shareD[3]);

        good.setOnClickListener(this);
        unlike.setOnClickListener(this);
        comment.setOnClickListener(this);
        collect.setOnClickListener(this);
        share.setOnClickListener(this);
        send.setOnClickListener(this);
        delete.setOnClickListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                try{
                    if(!comments.get(i).getU_name().equals("t1")){
                        isComment = false;
                        if(comments.get(i).getC_c_id().equals("0"))
                            s_c_id = comments.get(i).getId();
                        else
                            s_c_id = comments.get(i).getC_c_id();
                        r_id = comments.get(i).getU_id();
                        r_name = comments.get(i).getU_name();
                        r_img = comments.get(i).getU_img();
                        edit_frame.setVisibility(View.VISIBLE);
                        send.setVisibility(View.VISIBLE);
                        edit.setHint("回复"+r_name+":");
                        edit.requestFocus();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edit, InputMethodManager.RESULT_SHOWN);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }else{
                        delete.setVisibility(View.VISIBLE);
                        delete.setTag(comments.get(i).getId());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setVideo(){
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
        try {
            videoUrl = "http://192.168.154.1:8080/file/"+info.getString("file");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        //video.setVideoURI(Uri.parse("http://192.168.154.1:8080/file/2019跨年.mp4" ));
        video.setVideoPath(videoUrl);
        //设置视频缩略图
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoUrl, new HashMap());
        bitmap = retriever.getFrameAtTime();
        video.setBackground(new BitmapDrawable(getResources(),bitmap));
        mediaController = new MediaController(this);
        video.setMediaController(mediaController);
        mediaController.setMediaPlayer(video);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.good:
                Toast.makeText(TextDetail.this,"点赞",Toast.LENGTH_SHORT).show();
                break;
            case R.id.unlike:
                Toast.makeText(TextDetail.this,"不喜欢",Toast.LENGTH_SHORT).show();
                break;
            case R.id.comment:
                isComment = true;
                edit_frame.setVisibility(View.VISIBLE);
                send.setVisibility(View.VISIBLE);
                edit.setHint("输入评论...");
                edit.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edit, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                break;
            case R.id.collect:
                Toast.makeText(TextDetail.this,"收藏",Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Toast.makeText(TextDetail.this,"分享",Toast.LENGTH_SHORT).show();
                break;
            case R.id.send:
                String s = edit.getText().toString();
                edit.setText("");
//                Toast.makeText(TextDetail.this,"发送:"+s,Toast.LENGTH_SHORT).show();
                message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);

                createComment(s);
                break;
            case R.id.delete:
                params = new HashMap<String, String>();
                params.put("ID",delete.getTag().toString());
                httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/deleteStudyComment.do",params,new MyStringCallBack() {
                    @Override
                    public void onResponse(String response, int id) {
                        super.onResponse(response, id);
                        if(response.equals("Delete successfully")){
                            message = Message.obtain();
                            message.what = 1;
                            handler.sendMessage(message);
                            Toast.makeText(TextDetail.this,"删除成功",Toast.LENGTH_SHORT).show();

                            displayComments();
                        }else{
                            Toast.makeText(TextDetail.this,"删除失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    private void createComment(final String comment){
        final UserInfo UI = (UserInfo)getApplication();
        params = new HashMap<String, String>();
        try {
            params.put("s_r_id",info.getString("id"));
//            params.put("u_id",UI.getId());
//            params.put("u_name",UI.getName());
//            params.put("u_img",UI.getImg());
            params.put("u_id","1");
            params.put("u_name","t1");
            params.put("u_img","t1.jpg");
            if(isComment){
                params.put("s_c_id","0");
                params.put("r_u_id","0");
                params.put("r_u_name","0");
                params.put("r_u_img","0");
            }else{
                params.put("s_c_id",s_c_id);
                params.put("r_u_id",r_id);
                params.put("r_u_name",r_name);
                params.put("r_u_img",r_img);
            }
            params.put("comment",comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/createStudyComment.do",params,new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                if(response.equals("Create successfully")){
                    message = Message.obtain();
                    message.what = 2;
                    handler.sendMessage(message);
                    Toast.makeText(TextDetail.this,"评论成功",Toast.LENGTH_SHORT).show();
                    displayComments();
                }else{
                    Toast.makeText(TextDetail.this,"评论失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayComments(){
        try {
            comments.clear();
            params = new HashMap<String, String>();
            params.put("ID",info.getString("id"));
            httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/displayStudyComment.do",params,new MyStringCallBack() {
                @Override
                public void onResponse(String response, int id) {
                    super.onResponse(response, id);
                    try {
                        results = new JSONObject(response);
                        for(int i = 0;i<results.length();i++){
                            JO = results.getJSONObject(""+i);
                            comments.add(new Comment(JO));
                            replys = JO.getJSONObject("replys");
                            for(int j=0;j<replys.length();j++){
                                comments.add(new Comment(replys.getJSONObject(""+j)));
                            }
                        }
                        message = Message.obtain();
                        message.what = 2;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 1://收起编辑区和输入法
                        edit_frame.setVisibility(View.INVISIBLE);
                        delete.setVisibility(View.INVISIBLE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(imm != null)
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        break;
                    case 2://更新评论区
                        ca = new CommentAdapter(TextDetail.this,comments);
                        list.setAdapter(ca);
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
                message = Message.obtain();
                message.what = 1;
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
