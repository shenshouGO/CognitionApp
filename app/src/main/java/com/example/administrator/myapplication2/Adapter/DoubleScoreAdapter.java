package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.myapplication2.Bean.DoubleScore;
import com.example.administrator.myapplication2.R;

import java.util.List;

/**
 * Created by Administrator on 2021/1/25.
 */

public class DoubleScoreAdapter extends BaseAdapter {
    private Context mContext;
    private List<DoubleScore> mData = null;

    public DoubleScoreAdapter(Context mContext, List<DoubleScore> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<DoubleScore> mData){
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
        DoubleScoreAdapter.ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.scores,parent,false);
            holder = new DoubleScoreAdapter.ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.validity = (TextView) convertView.findViewById(R.id.validity);
            holder.novelty = (TextView) convertView.findViewById(R.id.novelty);
            holder.average = (TextView) convertView.findViewById(R.id.average);
            convertView.setTag(holder);
        }else{
            holder = (DoubleScoreAdapter.ViewHolder) convertView.getTag();
        }
        holder.name.setText(mData.get(position).getName());
        holder.validity.setText(mData.get(position).getValidity());
        holder.novelty.setText(mData.get(position).getNovelty());
        holder.average.setText(mData.get(position).getAverage());
        return convertView;
    }

    static class ViewHolder{
        TextView name;
        TextView validity;
        TextView novelty;
        TextView average;
    }
}
