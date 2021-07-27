package com.example.administrator.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.UserInfo;

/**
 * Created by Administrator on 2021/2/25.
 */

public class MyInfo extends Fragment implements View.OnClickListener{
    private Context context;
    private JSONObject Info;
    private ImageView img;
    private TextView name;
    private TextView medal;
    private TextView comment;
    private TextView focus;
    private TextView good;
    private TextView rank;
    private TextView userData;
    private TextView integral;
    private TextView security;
    private TextView out;
    private Intent intent;

    private HttpUtil httpUtil;
    private Map<String,String> params;
    final Handler handler = new MyHandler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_info, container, false);
        img = (ImageView)view.findViewById(R.id.img);
        name = (TextView) view.findViewById(R.id.name);
        medal = (TextView) view.findViewById(R.id.medal);
        comment = (TextView) view.findViewById(R.id.comment);
        focus = (TextView) view.findViewById(R.id.focus);
        good = (TextView) view.findViewById(R.id.good);
        rank = (TextView) view.findViewById(R.id.rank);
        userData = (TextView) view.findViewById(R.id.userdata);
        integral = (TextView) view.findViewById(R.id.integral);
        security = (TextView) view.findViewById(R.id.security);
        out = (TextView) view.findViewById(R.id.out);

        httpUtil = new HttpUtil();

        img.setOnClickListener(this);
        name.setOnClickListener(this);
        medal.setOnClickListener(this);
        comment.setOnClickListener(this);
        focus.setOnClickListener(this);
        good.setOnClickListener(this);
        rank.setOnClickListener(this);
        userData.setOnClickListener(this);
        integral.setOnClickListener(this);
        security.setOnClickListener(this);
        out.setOnClickListener(this);

        display();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        display();
    }

    private void display(){
        final UserInfo UI = (UserInfo)getActivity().getApplication();
        params = new HashMap<String, String>();
        params.put("sql","select * from user where id ="+UI.getId());
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
                    JSONObject user = new JSONObject(msg.obj.toString());
                    name.setText(user.getJSONObject("0").getString("name"));
                    Glide.with(MyInfo.this).load("http://59.110.215.154:8080/resource/"+user.getJSONObject("0").getString("img_name")).into(img);
                    int integral = user.getJSONObject("0").getInt("integral_sum");

                    switch ((integral-1)/200){
                        case 0:
                            medal.setText("初出茅庐");
                            break;
                        case 1:
                            medal.setText("崭露头角");
                            break;
                        case 2:
                            medal.setText("声名鹊起");
                            break;
                        case 3:
                            medal.setText("远近闻名");
                            break;
                        case 4:
                            medal.setText("闻名一方");
                            break;
                        case 5:
                            medal.setText("声名远扬");
                            break;
                        case 6:
                            medal.setText("名扬四海");
                            break;
                        case 7:
                            medal.setText("名震八方");
                            break;
                        case 8:
                        default:
                            medal.setText("名冠天下");
                            break;
                    }

                    comment.setText("0\n"+context.getResources().getString(R.string.comment));
                    focus.setText("0\n"+context.getResources().getString(R.string.focus));
                    good.setText("0\n"+context.getResources().getString(R.string.good));
                    rank.setText("0\n"+context.getResources().getString(R.string.rank));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.img:
                Toast.makeText(context,R.string.img,Toast.LENGTH_SHORT).show();
                break;
            case R.id.name:
                Toast.makeText(context,R.string.name,Toast.LENGTH_SHORT).show();
                break;
            case R.id.medal:
                Toast.makeText(context,R.string.medal,Toast.LENGTH_SHORT).show();
                break;
            case R.id.comment:
                Toast.makeText(context,R.string.comment,Toast.LENGTH_SHORT).show();
                break;
            case R.id.focus:
                Toast.makeText(context,R.string.focus,Toast.LENGTH_SHORT).show();
                break;
            case R.id.good:
                Toast.makeText(context,R.string.good,Toast.LENGTH_SHORT).show();
                break;
            case R.id.rank:
                Toast.makeText(context,R.string.rank,Toast.LENGTH_SHORT).show();
                break;
            case R.id.userdata:
                intent = new Intent(context,UserData.class);
                intent.putExtra("userId","0");
                startActivity(intent);
                break;
            case R.id.integral:
                intent = new Intent(context,Integral.class);
                startActivity(intent);
                break;
            case R.id.security:
                intent = new Intent(getActivity(),AccountSecurity.class);
                startActivity(intent);
                break;
            case R.id.out:
//                Toast.makeText(context,R.string.out,Toast.LENGTH_SHORT).show();
                //清除用户缓存信息
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();

                intent = new Intent(getActivity(),Login.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }

    public static final MyInfo newInstance(String info)
    {
        MyInfo fragment = new MyInfo();
        Bundle bundle = new Bundle();
        bundle.putString("info", info);
        fragment.setArguments(bundle);

        return fragment ;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Info = new JSONObject(getArguments().getString("info"));
            context = getContext();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
