package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.support.constraint.solver.Cache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication2.Bean.Resource;
import com.example.administrator.myapplication2.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.VedioAsyncTask;

/**
 * Created by Administrator on 2021/2/2.
 */

public class ScreenAdapter extends BaseAdapter {
    private List<Resource> mData = null;
    private Context mContext;
    private static final int TYPE_VIDEO = 0;
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;
    private HttpUtil httpUtil;
    private Map<String,String> params;

    public ScreenAdapter(Context context,List<Resource> mData){
        this.mContext = context;
        this.mData = mData;
        this.httpUtil = new HttpUtil();
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
        switch (mData.get(position).getType()){
            case "视频":
                return TYPE_VIDEO;
            case "文本":
                return TYPE_TEXT;
            default:
                return TYPE_IMAGE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        String path = "http://59.110.215.154:8080/resource/";
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.screen_item,parent,false);
            holder.img=(ImageView)convertView.findViewById(R.id.img);
            holder.title=(TextView)convertView.findViewById(R.id.title);
            holder.text=(TextView)convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        switch (type){
            case TYPE_VIDEO:
                holder.title.setText(mData.get(position).getTheme()+"|"+mData.get(position).getType());
                holder.img.setVisibility(View.VISIBLE);
                holder.text.setVisibility(View.INVISIBLE);
                holder.img.setImageResource(R.drawable.banner_default);
                VedioAsyncTask vedioAsyncTask = new VedioAsyncTask(holder.img);
                vedioAsyncTask.execute("http://59.110.215.154:8080/resource/"+mData.get(position).getPath());
                break;
            case TYPE_TEXT:
                holder.title.setText(mData.get(position).getTheme()+"|"+mData.get(position).getType());
                holder.text.setVisibility(View.VISIBLE);
                holder.img.setVisibility(View.INVISIBLE);
                params = new HashMap<String, String>();
                params.put("file",mData.get(position).getPath());
                httpUtil.postRequest("http://59.110.215.154:8080/CognitionAPP/read.do",params,new MyStringCallBack(holder) {
                    @Override
                    public void onResponse(String response, int id) {
                        String[] splits = response.split("\\|\\|\\|");
                        screenAdapter_holder.text.setText(splits[0]);
                    }
                });
                break;
            case TYPE_IMAGE:
                holder.title.setText(mData.get(position).getTheme()+"|"+mData.get(position).getType());
                holder.img.setVisibility(View.VISIBLE);
                holder.text.setVisibility(View.INVISIBLE);
                Glide.with(mContext).load(path+mData.get(position).getPath()).into(holder.img);
//                Picasso.with(mContext).load(path+mData.get(position).getPath()).into(holder.img);
                break;
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView title ;
        public ImageView img;
        public TextView text;
    }
}
