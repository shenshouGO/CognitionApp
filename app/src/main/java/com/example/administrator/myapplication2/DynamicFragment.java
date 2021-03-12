package com.example.administrator.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication2.Adapter.DynamicAdapter;
import com.example.administrator.myapplication2.Adapter.OtherCognitionsAdapter;
import com.example.administrator.myapplication2.Bean.OtherCognitions;

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

/**
 * Created by Administrator on 2021/3/6.
 */

public class DynamicFragment extends Fragment implements View.OnClickListener{
    private HttpUtil httpUtil;
    private Map<String,String> params;
    final Handler handler = new MyHandler();
    private Message m;
    private Intent intent;
    private JSONObject info;
    private JSONObject results;
    private List<OtherCognitions> resources;
    private DynamicAdapter da;
    private JSONObject JO;
    private Date date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private TextView message;
    private TextView focus;
    private TextView find;
    private ImageView release;
    private ImageView img;
    private TextView name;
    private ListView resourceScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dynamic_fragment, container, false);
        message = (TextView) view.findViewById(R.id.message);
        focus = (TextView) view.findViewById(R.id.focus);
        find = (TextView) view.findViewById(R.id.find);
        release = (ImageView) view.findViewById(R.id.release);
        img = (ImageView) view.findViewById(R.id.img);
        name = (TextView) view.findViewById(R.id.name);
        resourceScreen = (ListView) view.findViewById(R.id.resourceScreen);
        message.setOnClickListener(this);
        focus.setOnClickListener(this);
        find.setOnClickListener(this);
        release.setOnClickListener(this);

        final UserInfo UI = (UserInfo)getActivity().getApplication();
        name.setText(UI.getName());
        httpUtil = new HttpUtil();

        resourceScreen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                try{
                    intent = new Intent(getActivity(),SceneDetail.class);
                    intent.putExtra("info", results.getJSONObject(""+i).toString());
                    intent.putExtra("type","动态");
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.message:
//                intent = new Intent(getActivity(), PictureStory.class);
//                startActivity(intent);
                Toast.makeText(getActivity(),"消息",Toast.LENGTH_SHORT).show();
                break;
            case R.id.focus:
//                intent = new Intent(getActivity(), DescribeScene.class);
//                startActivity(intent);
                Toast.makeText(getActivity(),"关注",Toast.LENGTH_SHORT).show();
                break;
            case R.id.find:
                intent = new Intent(getActivity(), RankList.class);
                startActivity(intent);
//                Toast.makeText(getActivity(),"发现",Toast.LENGTH_SHORT).show();
                break;
            case R.id.release:
                Intent intent = new Intent(getActivity(), ReleaseScene.class);
                intent.putExtra("type","发布动态");
                startActivity(intent);
                break;
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    results = new JSONObject(msg.obj.toString());
                    resources = new LinkedList<OtherCognitions>() ;
                    for(int i = 0;i<results.length();i++){
                        JO = results.getJSONObject(""+i);
                        date = new Date(new Long(JO.getString("time")));
                        resources.add(new OtherCognitions(JO.getString("id"),JO.getString("u_img"),JO.getString("u_name"),JO.getString("file"),simpleDateFormat.format(date),JO.getString("good"),JO.getString("comment"),"1"));
                    }
                    da = new DynamicAdapter(getActivity(),resources);
                    resourceScreen.setAdapter(da);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        displayDynamic();
    }

    private void displayDynamic(){
        final UserInfo UI = (UserInfo)getActivity().getApplication();
        params = new HashMap<String, String>();
        params.put("ID",UI.getId());
        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/displayArticle.do",params,new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                Log.e("response:",response+" "+id);
                if(!response.equals("{}")){
                    Message message = new Message();
                    message.what = 1;
                    message.obj = response;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
