package com.example.administrator.myapplication2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.administrator.myapplication2.Adapter.UserRankAdapter;
import com.example.administrator.myapplication2.Bean.JsonBean;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;

/**
 * Created by Administrator on 2021/3/13.
 */

public class UserRankFragment  extends Fragment {
    private RelativeLayout head;
    private ListView resourceScreen;
    private JSONObject results;
    private JSONObject JO;
    private List<JsonBean> resources;
    private UserRankAdapter resourceAdapter;
    final Handler handler = new MyHandler();
    private HttpUtil httpUtil;
    private Map<String, String> params;
    private int type;

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
                try {
//                    intent = new Intent(getContext(),TextDetail.class);
//                    intent.putExtra("info", results.getJSONObject(""+i).toString());
//                    startActivity(intent);
                    Toast.makeText(getActivity(), i + "", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void displayResource() {
        params = new HashMap<String, String>();
        if(type == 0)
            params.put("sql", "select * from user order by integral_game desc");
        else
            params.put("sql", "select * from user order by integral_sum desc");
        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/displaySql.do", params, new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                super.onResponse(response, id);
                Log.e("response:", response + " " + id);
                if (!response.equals("{}")) {
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
                    resources = new LinkedList<JsonBean>();
                    for (int i = 0; i < results.length(); i++) {
                        JO = results.getJSONObject("" + i);
                        resources.add(new JsonBean(JO));
                    }
                    resourceAdapter = new UserRankAdapter(getActivity(), resources,type);
                    resourceScreen.setAdapter(resourceAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static final UserRankFragment newInstance(int type)
    {
        UserRankFragment fragment = new UserRankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);

        return fragment ;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getArguments().getInt("type");
    }
}
