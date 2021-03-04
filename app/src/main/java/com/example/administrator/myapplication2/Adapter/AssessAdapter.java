package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication2.Bean.Resource;
import com.example.administrator.myapplication2.R;

import java.util.List;

/**
 * Created by Administrator on 2021/3/4.
 */

public class AssessAdapter extends BaseAdapter {
    private Context mContext;
    private List<Resource> mData = null;
    private String[] split;

    public AssessAdapter(Context mContext, List<Resource> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<Resource> mData){
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
            holder.img.setVisibility(View.INVISIBLE);
            holder.name.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.GONE);
            holder.good.setVisibility(View.GONE);
            holder.comment.setVisibility(View.GONE);
            convertView.setTag(holder);
        }else{
            holder = (OtherCognitionsAdapter.ViewHolder) convertView.getTag();
        }
        String file = mData.get(position).getPath();
        split = file.split("\\.");
        holder.text.setText(split[0]);
        return convertView;
    }
}
