package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication2.Bean.OtherCognitions;
import com.example.administrator.myapplication2.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;

/**
 * Created by Administrator on 2021/3/8.
 */

public class DynamicAdapter extends BaseAdapter {
    private Context mContext;
    private List<OtherCognitions> mData = null;
    private HttpUtil httpUtil;
    private Map<String,String> params;
    private String[] split;

    public DynamicAdapter(Context mContext, List<OtherCognitions> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.httpUtil = new HttpUtil();
    }

    public void setData(List<OtherCognitions> mData){
        this.mData = mData;
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OtherCognitionsAdapter.ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.othercognition,parent,false);
            holder = new OtherCognitionsAdapter.ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.good = (TextView) convertView.findViewById(R.id.good);
            holder.comment = (TextView) convertView.findViewById(R.id.comment);
            convertView.setTag(holder);
        }else{
            holder = (OtherCognitionsAdapter.ViewHolder) convertView.getTag();
        }
        holder.img.setVisibility(View.GONE);
        holder.name.setVisibility(View.GONE);
        params = new HashMap<String, String>();
        params.put("file",mData.get(position).getText());
        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/read.do",params,new MyStringCallBack(holder) {
            @Override
            public void onResponse(String response, int id) {
                split = response.split("\\|\\|\\|");
                holder.text.setText(split[0]);
            }
        });
        holder.time.setText(mData.get(position).getTime());
//        holder.good.setText(mData.get(position).getGood()+"点赞");
//        holder.comment.setText(mData.get(position).getComment()+"评论");
        holder.good.setVisibility(View.GONE);
        holder.comment.setVisibility(View.GONE);
        return convertView;
    }
}
