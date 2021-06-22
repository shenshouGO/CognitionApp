package com.example.administrator.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication2.Adapter.MixedAdapter;
import com.example.administrator.myapplication2.Bean.OtherCognitions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;

public class SceneDetail extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    private ImageView back;
    private TextView title;

    private MixedAdapter mixedAdapter;
    private JSONObject results;
    private JSONObject JO;
    private JSONObject info;
    private List<OtherCognitions> resources;
    final Handler handler = new MyHandler();
    private HttpUtil httpUtil;
    private Map<String,String> params;
    private String url;
    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Intent intent;
    private LinearLayout ll;
    private EditText edit;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_detail);

        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        listView = (ListView)findViewById(R.id.list);
        httpUtil = new HttpUtil();
        intent = getIntent();
        if(intent.getStringExtra("type") !=null){
            title.setText("动态详情");
        }
        ll = (LinearLayout) findViewById(R.id.edit_frame);
        edit = (EditText) findViewById(R.id.edit);
        send = (Button) findViewById(R.id.send);
        ll.setVisibility(View.INVISIBLE);
        send.setOnClickListener(this);
        back.setOnClickListener(this);
        try {
            info = new JSONObject(intent.getStringExtra("info"));
            Log.e("Info:",info.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                try{
                    if(i != 0){
                        Log.e("Results:",results.toString());
                        intent = new Intent(SceneDetail.this,CognitionDetail.class);
                        intent.putExtra("info", results.getJSONObject(""+(i-1)).toString());
                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        try {
            final UserInfo UI = (UserInfo) getApplication();
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.send:
                    String s =edit.getText().toString();
                    edit.setText("");
                    params = new HashMap<String, String>();
                    params.put("ID",UI.getId());
                    params.put("u_name",UI.getName());
                    params.put("u_img",UI.getImg());
                    params.put("c_r_id",info.getString("id"));
                    params.put("c_r_file",info.getString("file"));
                    params.put("cognition",s);
                    httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/createCognition.do",params,new MyStringCallBack(){
                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("response:",response+" "+id);
                            if(response.equals("Create successfully")){
                                Toast.makeText(SceneDetail.this,"重评成功！",Toast.LENGTH_SHORT).show();
                                displayCognition();
                                Message message = new Message();
                                message.what = 2;
                                handler.sendMessage(message);
                            }else{
                                Toast.makeText(SceneDetail.this,"重评失败！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void discuss(){
        ll.setVisibility(View.VISIBLE);
        send.setVisibility(View.VISIBLE);
        edit.setHint("输入内容...");
        edit.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edit, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayCognition();
    }

    private void displayCognition(){
        params = new HashMap<String, String>();
        try {
            params.put("ID",info.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/displayCognition.do",params,new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                Log.e("response:",response+" "+id);
                Message message = new Message();
                message.what = 1;
                message.obj = response;
                handler.sendMessage(message);
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 1:
                        results = new JSONObject(msg.obj.toString());
                        resources = new LinkedList<OtherCognitions>() ;
                        date = new Date(new Long(info.getString("time")));
                        resources.add(new OtherCognitions(info.getString("id"),info.getString("u_img"),info.getString("u_name"),info.getString("file"),simpleDateFormat.format(date),info.getString("good"),info.getString("comment"),"0"));
                        for(int i = 0;i<results.length();i++){
                            JO = results.getJSONObject(""+i);
                            date = new Date(new Long(JO.getString("time")));
                            resources.add(new OtherCognitions(JO.getString("id"),JO.getString("u_img"),JO.getString("u_name"),JO.getString("file"),simpleDateFormat.format(date),JO.getString("good"),JO.getString("comment"),"1"));
                        }
//                        mixedAdapter = new MixedAdapter(SceneDetail.this,resources,((UserInfo) getApplication()).getId());
                        mixedAdapter = new MixedAdapter(SceneDetail.this,resources,"1");
                        listView.setAdapter(mixedAdapter);
                        break;
                    case 2:
                        ll.setVisibility(View.INVISIBLE);
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
