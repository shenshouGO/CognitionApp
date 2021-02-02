package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.myapplication2.Bean.Answer;
import com.example.administrator.myapplication2.R;

import java.util.List;

/**
 * Created by Administrator on 2021/1/22.
 */

public class AnswerAdapter extends BaseAdapter {
    private Context mContext;
    private List<Answer> mData = null;

    public AnswerAdapter(Context mContext,List<Answer> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<Answer> mData){
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
        AnswerAdapter.ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.answer,parent,false);
            holder = new AnswerAdapter.ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.answer = (TextView) convertView.findViewById(R.id.answer);
            convertView.setTag(holder);
        }else{
            holder = (AnswerAdapter.ViewHolder) convertView.getTag();
        }
        holder.name.setText(mData.get(position).getName()+"：");
        holder.answer.setText(mData.get(position).getAnswer());
        return convertView;
    }

    static class ViewHolder{
        TextView name;
        TextView answer;
    }
}
