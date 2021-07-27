package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
 * Created by Administrator on 2021/2/5.
 */

public class OtherCognitionsAdapter extends BaseAdapter {
    private Context mContext;
    private List<OtherCognitions> mData = null;
    private HttpUtil httpUtil;
    private Map<String,String> params;
    private String[] split;
    private int type;

    public OtherCognitionsAdapter(Context mContext, List<OtherCognitions> mData,int type) {
        this.mContext = mContext;
        this.mData = mData;
        this.httpUtil = new HttpUtil();
        this.type = type;
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
        holder.img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_launcher));
        holder.name.setText(mData.get(position).getName());
        params = new HashMap<String, String>();
        params.put("file",mData.get(position).getText());
//        holder.text.setText(mData.get(position).getText());
        httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/read.do",params,new MyStringCallBack(holder) {
            @Override
            public void onResponse(String response, int id) {
//                Log.e("response:",response+" "+id);
                split = response.split("\\|\\|\\|");
//                for(int i = 0;i<split.length;i++){
//                    Log.e("split:",i+" "+split[i]);
//                }
                holder.text.setText(split[0]);
            }
        });
        holder.time.setText(mData.get(position).getTime());
        holder.good.setText(mData.get(position).getGood()+"点赞");
        if(type == 0){
            holder.comment.setText(mData.get(position).getComment()+"评论");
        }else{
            holder.comment.setText(mData.get(position).getComment()+"重评");
        }
        return convertView;
    }

    public static class ViewHolder{
        ImageView img;
        TextView name;
        TextView text;
        TextView time;
        TextView good;
        TextView comment;
    }
}
