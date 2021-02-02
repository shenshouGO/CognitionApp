package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.myapplication2.R;
import com.example.administrator.myapplication2.Bean.ResultScore;

import java.util.List;

/**
 * Created by Administrator on 2021/1/26.
 */

public class ResultScoreAdapter extends BaseAdapter {
    private Context mContext;
    private List<ResultScore> mData = null;

    public ResultScoreAdapter(Context mContext,List<ResultScore> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<ResultScore> mData){
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
        ResultScoreAdapter.ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.result_score,parent,false);
            holder = new ResultScoreAdapter.ViewHolder();
            holder.one = (TextView) convertView.findViewById(R.id.one);
            holder.two = (TextView) convertView.findViewById(R.id.two);
            holder.three = (TextView) convertView.findViewById(R.id.three);
            holder.four = (TextView) convertView.findViewById(R.id.four);
            holder.five = (TextView) convertView.findViewById(R.id.five);
            convertView.setTag(holder);
        }else{
            holder = (ResultScoreAdapter.ViewHolder) convertView.getTag();
        }
        holder.one.setText(mData.get(position).getOne());
        holder.two.setText(mData.get(position).getTwo());
        holder.three.setText(mData.get(position).getThree());
        holder.four.setText(mData.get(position).getFour());
        holder.five.setText(mData.get(position).getFive());
        return convertView;
    }

    static class ViewHolder{
        TextView one;
        TextView two;
        TextView three;
        TextView four;
        TextView five;
    }
}
