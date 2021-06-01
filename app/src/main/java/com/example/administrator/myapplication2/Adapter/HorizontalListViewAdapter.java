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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.VedioAsyncTask;

/**
 * Created by Administrator on 2021/2/2.
 */

public class HorizontalListViewAdapter extends BaseAdapter {
    private List<Resource> mData = null;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;
    private static final int TYPE_VIDEO = 0;
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;
    private HttpUtil httpUtil;
    private Map<String,String> params;

    public HorizontalListViewAdapter(Context context,List<Resource> mData){
        this.mContext = context;
        this.mData = mData;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
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
        ScreenAdapter.ViewHolder holder;
        int type = getItemViewType(position);
        String path = "http://59.110.215.154:8080/resource/";
        if(convertView==null){
            holder = new ScreenAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder.img=(ImageView)convertView.findViewById(R.id.img);
            holder.title=(TextView)convertView.findViewById(R.id.title);
            holder.text=(TextView)convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else{
            holder=(ScreenAdapter.ViewHolder)convertView.getTag();
        }
        if(position == selectIndex){
            convertView.setSelected(true);
        }else{
            convertView.setSelected(false);
        }

        holder.title.setText(mData.get(position).getTitle());
        switch (type){
            case TYPE_VIDEO:
                holder.img.setVisibility(View.VISIBLE);
                holder.text.setVisibility(View.GONE);
                holder.img.setImageResource(R.drawable.banner_default);
                VedioAsyncTask vedioAsyncTask = new VedioAsyncTask(holder.img);
                vedioAsyncTask.execute("http://59.110.215.154:8080/resource/"+mData.get(position).getPath());
                break;
            case TYPE_TEXT:
                holder.text.setVisibility(View.VISIBLE);
                holder.img.setVisibility(View.GONE);
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
                holder.img.setVisibility(View.VISIBLE);
                holder.text.setVisibility(View.GONE);
                Glide.with(mContext).load(path+mData.get(position).getPath()).into(holder.img);
                break;
        }
        return convertView;
    }
    public void setSelectIndex(int i){
        selectIndex = i;
    }
}
