package com.example.administrator.myapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Administrator on 2021/1/15.
 */

public class RightUserAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> mData = null;

    public RightUserAdapter(Context mContext,List<User> mData) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.right_user,parent,false);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img.setBackgroundResource(mData.get(position).getImg());
        holder.name.setText(mData.get(position).getName());
        holder.num.setText(position+5+"");
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
