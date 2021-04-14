package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.myapplication2.Bean.IntegralData;
import com.example.administrator.myapplication2.R;

import java.util.List;

/**
 * Created by Administrator on 2021/4/9.
 */

public class IntegralAdapter extends BaseAdapter {
    private Context mContext;
    private List<IntegralData> mData = null;

    public IntegralAdapter(Context mContext, List<IntegralData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<IntegralData> mData){
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
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.integral_item,parent,false);
            holder = new ViewHolder();
            holder.integral = (TextView) convertView.findViewById(R.id.integral);
            holder.source = (TextView) convertView.findViewById(R.id.integral_source);
            holder.time = (TextView) convertView.findViewById(R.id.integral_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.integral.setText(mData.get(position).getIntegral());
        holder.source.setText(mData.get(position).getSource());
        holder.time.setText(mData.get(position).getTime());
        return convertView;
    }

    public static class ViewHolder{
        TextView integral;
        TextView source;
        TextView time;
    }
}
