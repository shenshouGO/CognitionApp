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
import com.example.administrator.myapplication2.Bean.JsonBean;
import com.example.administrator.myapplication2.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.VedioAsyncTask;

/**
 * Created by Administrator on 2021/3/12.
 */

public class ResourceRankAdapter extends BaseAdapter {
    private Context mContext;
    private List<JsonBean> mData = null;
    private HttpUtil httpUtil;
    private Map<String,String> params;
    private static final int VEDIO = 0;
    private static final int TEXT = 1;
    private static final int IMAGE = 2;

    public ResourceRankAdapter(Context mContext, List<JsonBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.httpUtil = new HttpUtil();
    }

    public void setData(List<JsonBean> mData){
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
    public int getItemViewType(int position) {
        try {
            switch (mData.get(position).getJO().getString("type")){
                case "视频":
                    return VEDIO;
                case "文本":
                    return TEXT;
                case "图片":
                    return IMAGE;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            int type = getItemViewType(position);
            HotCognitionAdapter.ViewHolder holder1 = null;

            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.rank_item,parent,false);
                holder1 = new HotCognitionAdapter.ViewHolder();
                holder1.rank = (TextView) convertView.findViewById(R.id.rank);
                holder1.img_resource = (ImageView) convertView.findViewById(R.id.img_resource);
                holder1.img_pic = (ImageView) convertView.findViewById(R.id.img_pic);
                holder1.title = (TextView) convertView.findViewById(R.id.title);
                holder1.text = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder1);
            }else{
                holder1 = (HotCognitionAdapter.ViewHolder) convertView.getTag();
            }
            JSONObject JO = mData.get(position).getJO();
            String file = JO.getString("file");
            String[] split = file.split("\\.");
            switch (type){
                case VEDIO:
                    Log.e("视频",file);
                    holder1.rank.setText(position+1+"");
                    holder1.title.setVisibility(View.VISIBLE);
                    holder1.title.setText(split[0]);
                    holder1.img_resource.setVisibility(View.VISIBLE);
                    holder1.img_resource.setImageResource(R.drawable.banner_default);
                    holder1.img_pic.setVisibility(View.GONE);
                    VedioAsyncTask vedioAsyncTask = new VedioAsyncTask(holder1.img_resource);
                    vedioAsyncTask.execute("http://192.168.154.1:8080/file/"+file);
                    break;
                case TEXT:
                    Log.e("文本",file);
                    holder1.img_resource.setVisibility(View.GONE);
                    holder1.img_pic.setVisibility(View.GONE);
                    holder1.rank.setText(position+1+"");
                    holder1.text.setVisibility(View.INVISIBLE);
                    holder1.text.setHeight(150);
                    holder1.title.setVisibility(View.VISIBLE);
                    holder1.title.setText(split[0]);
                    break;
                case IMAGE:
                    Log.e("图片",file);
                    holder1.rank.setText(position+1+"");
                    holder1.title.setVisibility(View.GONE);
                    holder1.img_resource.setVisibility(View.GONE);
                    holder1.img_pic.setVisibility(View.VISIBLE);
                    holder1.img_pic.setImageResource(R.drawable.banner_default);
                    Glide.with(mContext).load("http://192.168.154.1:8080/file/"+file).into(holder1.img_pic);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
