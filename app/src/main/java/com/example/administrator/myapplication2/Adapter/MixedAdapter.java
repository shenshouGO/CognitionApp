package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication2.Bean.OtherCognitions;
import com.example.administrator.myapplication2.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.NineGridLayout;

/**
 * Created by Administrator on 2021/2/19.
 */

public class MixedAdapter extends BaseAdapter implements View.OnClickListener{
    private Context mContext;
    private List<OtherCognitions> mData = null;
    private static final int MAJOR = 0;
    private static final int OTHER = 1;
    private HttpUtil httpUtil;
    private Map<String,String> params;
    private String[] split;
    private List<String> imgs;
    private String s;

    public MixedAdapter(Context mContext,List<OtherCognitions> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.httpUtil = new HttpUtil();
        ImageLoader imageLoader;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
    }

    public void setData(List<OtherCognitions> mData){
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
        if (mData.get(position).getType().equals("0")) {
            return MAJOR;
        } else{
            return OTHER;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder1 holder1 = null;
        OtherCognitionsAdapter.ViewHolder holder2 = null;
        if(convertView == null){
            if(type == 0){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.cognition_item,parent,false);
                holder1 = new ViewHolder1();
                holder1.img = (ImageView) convertView.findViewById(R.id.img);
                holder1.name = (TextView) convertView.findViewById(R.id.name);
                holder1.text = (TextView) convertView.findViewById(R.id.text);
                holder1.imgs = (NineGridLayout) convertView.findViewById(R.id.imgs);
                holder1.time = (TextView) convertView.findViewById(R.id.time);
                holder1.discuss = (ImageView) convertView.findViewById(R.id.discuss);
                holder1.good = (ImageView) convertView.findViewById(R.id.good);
                holder1.share = (ImageView) convertView.findViewById(R.id.share);
                holder1.score = (Button) convertView.findViewById(R.id.score);
                convertView.setTag(R.string.holder1,holder1);
            }else{
                convertView = LayoutInflater.from(mContext).inflate(R.layout.othercognition,parent,false);
                holder2 = new OtherCognitionsAdapter.ViewHolder();
                holder2.img = (ImageView) convertView.findViewById(R.id.img);
                holder2.name = (TextView) convertView.findViewById(R.id.name);
                holder2.text = (TextView) convertView.findViewById(R.id.text);
                holder2.time = (TextView) convertView.findViewById(R.id.time);
                holder2.good = (TextView) convertView.findViewById(R.id.good);
                holder2.comment = (TextView) convertView.findViewById(R.id.comment);
                convertView.setTag(R.string.holder2,holder2);
            }
        }else{
            if(type == 0){
                holder1 = (ViewHolder1) convertView.getTag(R.string.holder1);
            }else{
                holder2 = (OtherCognitionsAdapter.ViewHolder) convertView.getTag(R.string.holder2);
            }
        }
        params = new HashMap<String, String>();
        s = mData.get(position).getText();
        params.put("file",s);
        s = s.substring(0,s.indexOf("."));

        switch (type){
            case MAJOR:
                holder1.img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_launcher));
                holder1.name.setText(mData.get(position).getName());
                httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/read.do",params,new MyStringCallBack(holder1) {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("response:",response+" "+id);
                        split = response.split("\\|\\|\\|");
                        Log.e("Inside split length:",""+split.length);
                        holder1.text.setText(split[0]);
                        if(split.length>1){
                            split = split[1].split("\\|");
                            imgs = new ArrayList<String>();
                            for(int i = 0;i<split.length;i++){
                                imgs.add("http://192.168.154.1:8080/file/"+s+"/"+split[i]);
                                Log.e("imgs:",i+" "+split[i]);
                            }
                            holder1.imgs.setIsShowAll(false);
                            holder1.imgs.setSpacing(10);
                            holder1.imgs.setUrlList(imgs);
                        }
                    }
                });
                holder1.time.setText(mData.get(position).getTime());
                holder1.discuss.setOnClickListener(this);
                holder1.good.setOnClickListener(this);
                holder1.score.setVisibility(View.GONE);
                holder1.share.setVisibility(View.GONE);

                break;
            case OTHER:
                holder2.img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_launcher));
                holder2.name.setText(mData.get(position).getName());
                httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/read.do",params,new MyStringCallBack(holder2) {
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("response:",response+" "+id);
                        split = response.split("\\|\\|\\|");
                        holder.text.setText(split[0]);
                    }
                });
                holder2.time.setText(mData.get(position).getTime());
                holder2.good.setText(mData.get(position).getGood()+"点赞");
                holder2.comment.setText(mData.get(position).getComment()+"评论");
                break;
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.discuss:
                Toast.makeText(mContext,"discuss",Toast.LENGTH_SHORT).show();
                break;
            case R.id.good:
                Toast.makeText(mContext,"good",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static class ViewHolder1{
        ImageView img;
        TextView name;
        TextView text;
        NineGridLayout imgs;
        TextView time;
        ImageView discuss;
        ImageView good;
        ImageView share;
        Button score;
    }
}
