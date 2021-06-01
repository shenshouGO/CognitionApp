package com.example.administrator.myapplication2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication2.Adapter.CommentAdapter;
import com.example.administrator.myapplication2.Bean.Comment;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import MyClass.HttpUtil;
import MyClass.InternetRequest;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;

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
    private float valid;
    private float novel;
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
    private LinearLayout ll;
    private EditText edit;
    private Button send;
    private boolean isComment;
    private boolean isScore;
    private JSONObject scoreJO;
    private String goodId;
    private String c_c_id;
    private String r_id;
    private String r_name;
    private String r_img;
    private Button delete;
    private ThreadPoolExecutor tpe;
    private HttpUtil httpUtil;
    private Map<String,String> params;
    private String[] split;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cognition_detail);

        final UserInfo UI = (UserInfo)getApplication();

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
        ll = (LinearLayout) findViewById(R.id.edit_frame);
        edit = (EditText) findViewById(R.id.edit);
        send = (Button) findViewById(R.id.send);
        delete = (Button) findViewById(R.id.delete);
        comments = new LinkedList<Comment>() ;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ll.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        tpe = new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(128));
        isScore = false;
        httpUtil = new HttpUtil();

        intent = this.getIntent();
        try{
            info = new JSONObject(intent.getStringExtra("info"));
            img.setOnClickListener(this);
            name.setText(info.getString("u_name"));
            name.setOnClickListener(this);
            pic.setVisibility(View.GONE);
//            text.setText(info.getString("file"));
            params = new HashMap<String, String>();
            params.put("file",info.getString("file"));
            httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/read.do",params,new MyStringCallBack() {
                @Override
                public void onResponse(String response, int id) {
                    Log.e("response:",response+" "+id);
                    split = response.split("\\|\\|\\|");
                    text.setText(split[0]);
                }
            });
            time.setText(simpleDateFormat.format(new Date(new Long(info.getString("time")))));
            back.setOnClickListener(this);
            discuss.setOnClickListener(this);
            good.setOnClickListener(this);
            share.setOnClickListener(this);
            score.setOnClickListener(this);
            edit.setOnClickListener(this);
            send.setOnClickListener(this);
            delete.setOnClickListener(this);

            IR = new InternetRequest();
            isGood();
            displayComments();
        }catch (Exception e){
            e.printStackTrace();
        }

        comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Log.e("comments",""+i);
                try{
                    if(!comments.get(i).getU_name().equals(UI.getName())){
                        isComment = false;
                        if(comments.get(i).getC_c_id().equals("0"))
                            c_c_id = comments.get(i).getId();
                        else
                            c_c_id = comments.get(i).getC_c_id();
                        r_id = comments.get(i).getU_id();
                        r_name = comments.get(i).getU_name();
                        r_img = comments.get(i).getU_img();
                        ll.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v){
        try{
            final UserInfo UI = (UserInfo)getApplication();
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.score:
                    final LayoutInflater inflater = CognitionDetail.this.getLayoutInflater();
                    View ds = inflater.inflate(R.layout.double_score, null,false);
                    TextView name = ds.findViewById(R.id.name);
                    ListView content = ds.findViewById(R.id.content);
                    content.setVisibility(View.GONE);
                    validity = ds.findViewById(R.id.validity);
                    novelty = ds.findViewById(R.id.novelty);
                    Button OK = ds.findViewById(R.id.OK);
                    OK.setOnClickListener(this);
                    if(isScore){
                        valid = Float.parseFloat(String.format("%.1f",info.getDouble("validity_score")));
                        novel = Float.parseFloat(String.format("%.1f",info.getDouble("novelty_score")));
                        name.setText("有效性："+valid+"   新颖性："+novel);
                        Log.e("Score:","valid = "+valid+" novel = "+novel);
                        validity.setRating(valid);
                        novelty.setRating(novel);
                        validity.setIsIndicator(true);
                        novelty.setIsIndicator(true);
                    }else{
                        name.setVisibility(View.GONE);
                    }

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
                    if(!isScore){
                        valid = (float) validity.getRating();
                        novel = (float)novelty.getRating();
                        createScore();
                    }
//                    Toast.makeText(CognitionDetail.this,"有效性："+valid+" 创新性："+novel,Toast.LENGTH_SHORT).show();
                    alert.dismiss();
                    break;
                case R.id.discuss:
                    isComment = true;
//                    Toast.makeText(CognitionDetail.this,"评论",Toast.LENGTH_SHORT).show();
                    ll.setVisibility(View.VISIBLE);
                    send.setVisibility(View.VISIBLE);
                    edit.setHint("输入评论...");
                    edit.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edit, InputMethodManager.RESULT_SHOWN);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                    break;
                case R.id.good:
                    Good();
                    break;
                case R.id.share:
                    Toast.makeText(CognitionDetail.this,"分享",Toast.LENGTH_SHORT).show();

                    Intent share_intent = new Intent();
                    share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                    share_intent.setType("text/plain");//设置分享内容的类型
                    share_intent.putExtra(Intent.EXTRA_SUBJECT, "share");//添加分享内容标题
                    share_intent.putExtra(Intent.EXTRA_TEXT, "share with you:"+"android");//添加分享内容
                    //创建分享的Dialog
                    share_intent = Intent.createChooser(share_intent, "share");
                    startActivity(share_intent);

                    break;
                case R.id.img:
                    Toast.makeText(CognitionDetail.this,"用户"+info.getString("u_name")+"的头像",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.name:
                    Toast.makeText(CognitionDetail.this,"用户"+info.getString("u_name")+"的昵称",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.send:
//                    Toast.makeText(CognitionDetail.this,"isComment:"+isComment,Toast.LENGTH_SHORT).show();
                    IR.addPara("u_name",UI.getName());
                    String s =edit.getText().toString();
                    edit.setText("");
                    createComment(s);
                    break;
                case R.id.delete:
                    IR.addPara("ID",delete.getTag().toString());
                    deleteComment();
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
            Log.e("what",msg.what+"");
            try {
                switch (msg.what){
                    case 1://更新评论区
                        ca = new CommentAdapter(CognitionDetail.this,comments);
                        comment.setAdapter(ca);
//                        edit.setText("");
                        break;
                    case 2://收起编辑区和输入法
                        ll.setVisibility(View.INVISIBLE);
                        delete.setVisibility(View.INVISIBLE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(imm != null)
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        break;
                    case 3://更新点赞状态
                        switch (str){
                            case "Delete unsuccessfully":
                                Toast.makeText(CognitionDetail.this,"取消点赞失败",Toast.LENGTH_SHORT).show();
                                break;
                            case "Delete successfully":
                                good.setImageDrawable(ContextCompat.getDrawable(CognitionDetail.this,R.drawable.disgood));
                                Toast.makeText(CognitionDetail.this,"取消点赞成功",Toast.LENGTH_SHORT).show();
                                goodId = "0";
                                break;
                            case "Give good unsuccessfully":
                                Toast.makeText(CognitionDetail.this,"点赞失败",Toast.LENGTH_SHORT).show();
                                goodId = "0";
                                break;
                            default:
                                goodId = str;
                                good.setImageDrawable(ContextCompat.getDrawable(CognitionDetail.this,R.drawable.isgood));
                                Toast.makeText(CognitionDetail.this,"点赞成功",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case 4://初始化点赞和评分区
                        if(!goodId.equals("0")){
                            good.setImageDrawable(ContextCompat.getDrawable(CognitionDetail.this,R.drawable.isgood));
                            Log.e("started goodId",goodId);
                        }else{
                            good.setImageDrawable(ContextCompat.getDrawable(CognitionDetail.this,R.drawable.disgood));
                            Log.e("started goodId",goodId);
                        }
                        if(isScore){
                            score.setText("查看评分");
                            valid = Float.parseFloat(scoreJO.getString("validity_score"));
                            novel = Float.parseFloat(scoreJO.getString("novelty_score"));
                        }
                        break;
                    case 5://发送提示消息
                        Toast.makeText(CognitionDetail.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                        break;
                    case 6://更新评分消息
                        if(str.equals("Unsuccessfully")){
                            Toast.makeText(CognitionDetail.this,"评分失败",Toast.LENGTH_SHORT).show();
                        }else{
                            info = new JSONObject(str);

                            Toast.makeText(CognitionDetail.this,"评分成功",Toast.LENGTH_SHORT).show();
                            score.setText("查看评分");
                            isScore = true;
                        }
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void createScore(){
        final UserInfo UI = (UserInfo)getApplication();
        tpe.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    IR.addPara("c_r_id",info.getString("id"));
                    IR.addPara("u_id",UI.getId());
                    IR.addPara("validity_score",""+(int)valid);
                    IR.addPara("novelty_score",""+(int)novel);
                    str = IR.requestPost("http://59.110.215.154:8080/CognitionAPP/createScore.do");

                    message = Message.obtain();
                    message.what = 6;
                    message.obj = str;
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void Good(){
        final UserInfo UI = (UserInfo)getApplication();
        tpe.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if(goodId.equals("0")){
                        IR.addPara("c_id",info.getString("id"));
                        IR.addPara("u_id",UI.getId());
                        str = IR.requestPost("http://59.110.215.154:8080/CognitionAPP/createCognitionGood.do");
                    }else{
                        IR.addPara("ID",goodId);
                        str = IR.requestPost("http://59.110.215.154:8080/CognitionAPP/deleteCognitionGood.do");
                    }

                    message = Message.obtain();
                    message.what = 3;
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void isGood(){
        final UserInfo UI = (UserInfo)getApplication();
        tpe.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    IR.addPara("c_id",info.getString("id"));
                    IR.addPara("u_id",UI.getId());
                    str = IR.requestPost("http://59.110.215.154:8080/CognitionAPP/matchUserAndCognitionGood.do");

                    if(str.equals("No give good"))
                        goodId = "0";
                    else
                        goodId = str;
                    Log.e("goodId",goodId);

                    IR.addPara("c_r_id",info.getString("id"));
                    IR.addPara("u_id",UI.getId());
                    str = IR.requestPost("http://59.110.215.154:8080/CognitionAPP/matchUserAndScore.do");

                    if(str.equals("Unevaluated")){
                        isScore =false;
                        scoreJO = null;
                    }
                    else{
                        isScore =true;
                        scoreJO = new JSONObject(str);
                        Log.e("scoreJO",scoreJO.toString());
                    }

                    message = Message.obtain();
                    message.what = 4;
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void displayComments(){
        tpe.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("ID",info.getString("id"));
                    comments.clear();
                    IR.addPara("ID",info.getString("id"));
                    str = IR.requestPost("http://59.110.215.154:8080/CognitionAPP/displayComment.do");
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
        });
    }

    private void createComment(final String comment){
        final UserInfo UI = (UserInfo)getApplication();
        tpe.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    IR.addPara("c_r_id",info.getString("id"));
                    IR.addPara("u_id",UI.getId());
//                    IR.addPara("u_name",UI.getName());
                    IR.addPara("u_img","null.jpg");
                    if(isComment){
                        IR.addPara("c_c_id","0");
                        IR.addPara("r_u_id","0");
                        IR.addPara("r_u_name","0");
                        IR.addPara("r_u_img","0");
                    }else{
                        IR.addPara("c_c_id",c_c_id);
                        IR.addPara("r_u_id",r_id);
                        IR.addPara("r_u_name",r_name);
                        IR.addPara("r_u_img",r_img);
                    }
                    IR.addPara("comment",comment);
                    str = IR.requestPost("http://59.110.215.154:8080/CognitionAPP/createComment.do");

                    Looper.prepare();
                    if(str.equals("Create successfully")){
                        message = Message.obtain();
                        message.what = 2;
                        handler.sendMessage(message);
                        Toast.makeText(CognitionDetail.this,"评论成功",Toast.LENGTH_SHORT).show();
                        displayComments();
                    }else{
                        Toast.makeText(CognitionDetail.this,"评论失败",Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void deleteComment(){
        tpe.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    str = IR.requestPost("http://59.110.215.154:8080/CognitionAPP/deleteComment.do");

                    Looper.prepare();
                    if(str.equals("Delete successfully")){
                        message = Message.obtain();
                        message.what = 2;
                        handler.sendMessage(message);
                        Toast.makeText(CognitionDetail.this,"删除成功",Toast.LENGTH_SHORT).show();
                        displayComments();
                    }else{
                        Toast.makeText(CognitionDetail.this,"删除失败",Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    //隐藏输入框
    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if(v!=null&&isShouldHideInput(ev)){
                message = Message.obtain();
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
        ll.getLocationOnScreen(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + ll.getHeight();
        int right = left + ll.getWidth();
        float locationX = pEv.getRawX();
        float locationY = pEv.getRawY();
        if (locationX > left && locationX < right
                && locationY > top && locationY < bottom) {
            // 点击的是输入框区域，保留点击EditText事件
            isHidden = false;
        } else {
            // 失去焦点
            ll.clearFocus();
            isHidden = true;
        }
        return isHidden;
    }
}
