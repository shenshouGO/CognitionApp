package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication2.R;
import com.example.administrator.myapplication2.Bean.User;

import java.util.List;

/**
 * Created by Administrator on 2021/1/15.
 */

public class LeftUserAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> mData = null;
    private String path = "http://192.168.154.1:8080/file/";

    public LeftUserAdapter(Context mContext,List<User> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<User> mData){
        this.mData = mData;
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
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.left_user,parent,false);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(mData.get(position).getImg().equals(" ")){
            holder.img.setBackgroundResource(R.drawable.p);
            Log.e("img:","null");
        }else{
            Log.e("img:",path+mData.get(position).getImg());
            Glide.with(mContext).load(path+mData.get(position).getImg()).into(holder.img);
        }
        holder.name.setText(mData.get(position).getName());
        holder.num.setText(position+1+"");
        holder.status.setText(mData.get(position).getStatus());
        return convertView;
    }

    static class ViewHolder{
        ImageView img;
        TextView name;
        TextView num;
        TextView status;
    }
}
