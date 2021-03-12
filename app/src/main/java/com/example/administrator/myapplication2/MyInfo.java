package com.example.administrator.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
    private TextView integral;
    private TextView help;
    private TextView out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final UserInfo UI = (UserInfo)getActivity().getApplication();
        View view = inflater.inflate(R.layout.my_info, container, false);
        img = (ImageView)view.findViewById(R.id.img);
        name = (TextView) view.findViewById(R.id.name);
        medal = (TextView) view.findViewById(R.id.medal);
        comment = (TextView) view.findViewById(R.id.comment);
        focus = (TextView) view.findViewById(R.id.focus);
        good = (TextView) view.findViewById(R.id.good);
        rank = (TextView) view.findViewById(R.id.rank);
        integral = (TextView) view.findViewById(R.id.integral);
        help = (TextView) view.findViewById(R.id.help);
        out = (TextView) view.findViewById(R.id.out);
        name.setText(UI.getName());
        comment.setText("0\n"+context.getResources().getString(R.string.comment));
        focus.setText("0\n"+context.getResources().getString(R.string.focus));
        good.setText("0\n"+context.getResources().getString(R.string.good));
        rank.setText("0\n"+context.getResources().getString(R.string.rank));

        img.setOnClickListener(this);
        name.setOnClickListener(this);
        medal.setOnClickListener(this);
        comment.setOnClickListener(this);
        focus.setOnClickListener(this);
        good.setOnClickListener(this);
        rank.setOnClickListener(this);
        integral.setOnClickListener(this);
        help.setOnClickListener(this);
        out.setOnClickListener(this);

        return view;
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
            case R.id.integral:
                Toast.makeText(context,R.string.integral,Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:
                Toast.makeText(context,R.string.help,Toast.LENGTH_SHORT).show();
                break;
            case R.id.out:
//                Toast.makeText(context,R.string.out,Toast.LENGTH_SHORT).show();
                //清除用户缓存信息
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(getActivity(),Login.class);
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
