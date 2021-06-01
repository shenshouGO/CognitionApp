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
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.example.administrator.myapplication2.Adapter.ResourceRankAdapter;
import com.example.administrator.myapplication2.Bean.JsonBean;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;

/**
 * Created by Administrator on 2021/3/12.
 */

public class ResourceRankFragment  extends Fragment {
    private RelativeLayout head;
    private ListView resourceScreen;
    private JSONObject results;
    private JSONObject JO;
    private List<JsonBean> resources;
    private ResourceRankAdapter resourceAdapter;
    final Handler handler = new MyHandler();
    private HttpUtil httpUtil;
    private Map<String,String> params;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_describe_scene, container, false);
        head = (RelativeLayout) view.findViewById(R.id.head);
        resourceScreen = (ListView) view.findViewById(R.id.resourceScreen);
        head.setVisibility(View.GONE);
        httpUtil = new HttpUtil();
        displayResource();

        resourceScreen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                try{
                    intent = new Intent(getContext(),RankResource.class);
                    intent.putExtra("info", results.getJSONObject(""+i).toString());
                    intent.putExtra("type", "材料榜单");
                    intent.putExtra("rank", i+1+"");
                    startActivity(intent);
//                    Toast.makeText(getActivity(),i+"",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void displayResource(){
        params = new HashMap<String, String>();
        params.put("sql","select * from cognition_resource where unit = 0 order by score desc limit 10");
        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/displaySql.do",params,new MyStringCallBack() {
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

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    results = new JSONObject(msg.obj.toString());
                    resources = new LinkedList<JsonBean>() ;
                    for(int i = 0;i<results.length();i++){
                        JO = results.getJSONObject(""+i);
                        resources.add(new JsonBean(JO));
                    }
                    resourceAdapter = new ResourceRankAdapter(getActivity(),resources);
                    resourceScreen.setAdapter(resourceAdapter);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
