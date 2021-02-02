package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Administrator on 2021/1/15.
 */

public class MAdapter<T> extends BaseAdapter {
    private Context mContext;
    private LinkedList<T> mData;

    public MAdapter(LinkedList<T> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
//            holder = new ViewHolder();
//            holder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
//            holder.txt_content = (TextView) convertView.findViewById(R.id.txt_content);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.img_icon.setImageResource(mData.get(position).getImgId());
//        holder.txt_content.setText(mData.get(position).getContent());
        return convertView;
    }

    private class ViewHolder {
        ImageView img_icon;
        TextView txt_content;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
