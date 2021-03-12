package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication2.Bean.OtherCognitions;
import com.example.administrator.myapplication2.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;

/**
 * Created by Administrator on 2021/3/12.
 */

public class HotCognitionAdapter extends BaseAdapter {
    private Context mContext;
    private List<OtherCognitions> mData = null;
    private HttpUtil httpUtil;
    private Map<String,String> params;
    private String[] split;

    public HotCognitionAdapter(Context mContext, List<OtherCognitions> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.httpUtil = new HttpUtil();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rank_item,parent,false);
            holder = new ViewHolder();
            holder.rank = (TextView) convertView.findViewById(R.id.rank);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        params = new HashMap<String, String>();
        params.put("file",mData.get(position).getText());
//        holder.text.setText(mData.get(position).getText());
        httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/read.do",params,new MyStringCallBack(position,holder) {
            @Override
            public void onResponse(String response, int id) {
                split = response.split("\\|\\|\\|");
                holder2.rank.setText(position+1+"");
                holder2.title.setText(split[0]);
            }
        });
        return convertView;
    }

    public static class ViewHolder{
        TextView rank;
        ImageView img_resource;
        ImageView img_head;
        TextView title;
        TextView text;
    }
}
