package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication2.Bean.Resource;
import com.example.administrator.myapplication2.R;

import java.util.List;

/**
 * Created by Administrator on 2021/2/2.
 */

public class ScreenAdapter extends BaseAdapter {
    private List<Resource> mData = null;
    private Context mContext;

    public ScreenAdapter(Context context,List<Resource> mData){
        this.mContext = context;
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
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.screen_item,parent,false);
            holder.img=(ImageView)convertView.findViewById(R.id.img);
            holder.title=(TextView)convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.title.setText(mData.get(position).getTitle());
//        holder.img.setImageBitmap();
        Glide.with(mContext).load(mData.get(position).getPath()).into(holder.img);
        return convertView;
    }

    private static class ViewHolder {
        private TextView title ;
        private ImageView img;
    }
}
