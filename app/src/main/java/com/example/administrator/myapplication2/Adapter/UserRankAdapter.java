package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
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

/**
 * Created by Administrator on 2021/3/13.
 */

public class UserRankAdapter  extends BaseAdapter {
    private Context mContext;
    private List<JsonBean> mData = null;
    private int type;
    private static final int GAME = 0;
    private static final int SUM = 1;
    private String path = "http://192.168.154.1:8080/file/";

    public UserRankAdapter(Context mContext, List<JsonBean> mData,int type) {
        this.mContext = mContext;
        this.mData = mData;
        this.type = type;
    }

    public void setData(List<JsonBean> mData) {
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
        HotCognitionAdapter.ViewHolder holder = null;
        JSONObject JO = mData.get(position).getJO();
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rank_item,parent,false);
            holder = new HotCognitionAdapter.ViewHolder();
            holder.rank = (TextView) convertView.findViewById(R.id.rank);
            holder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else{
            holder = (HotCognitionAdapter.ViewHolder) convertView.getTag();
        }

        try {
            holder.rank.setText(position+1+"");
            holder.img_head.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(path+JO.getString("img_name")).into(holder.img_head);
            holder.title.setText(JO.getString("name"));
            holder.text.setVisibility(View.VISIBLE);
            switch (type){
                case GAME:
                    holder.text.setText(JO.getString("integral_game"));
                    break;
                case SUM:
                    holder.text.setText(JO.getString("integral_sum"));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
