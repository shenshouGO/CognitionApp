package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication2.Bean.Resource;
import com.example.administrator.myapplication2.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MyClass.HttpUtil;
import MyClass.MyStringCallBack;
import MyClass.VedioAsyncTask;

/**
 * Created by Administrator on 2021/3/1.
 */

public class ResourceAdapter extends BaseAdapter {
    private Context mContext;
    private List<Resource> mData = null;
    private HttpUtil httpUtil;
    private Map<String,String> params;
    private String[] split;
    private static final int TYPE_VEDIO = 0;
    private static final int TYPE_TEXT = 1;

    public ResourceAdapter(Context mContext, List<Resource> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.httpUtil = new HttpUtil();
    }

    public void setData(List<Resource> mData){
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
        if (mData.get(position).getTitle().equals("视频")) {
            return TYPE_VEDIO;
        } else{
            return TYPE_TEXT;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder holder1 = null;
        OtherCognitionsAdapter.ViewHolder holder2 = null;
        if(convertView == null){
            if(type == 0){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.vedio_item,parent,false);
                holder1 = new ViewHolder();
                holder1.img = (ImageView) convertView.findViewById(R.id.img);
                holder1.text = (TextView) convertView.findViewById(R.id.text);
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
                holder2.img.setVisibility(View.GONE);
                holder2.time.setVisibility(View.GONE);
                holder2.good.setVisibility(View.GONE);
                holder2.comment.setVisibility(View.GONE);
                convertView.setTag(R.string.holder2,holder2);
            }
        }else{
            if(type == 0){
                holder1 = (ViewHolder) convertView.getTag(R.string.holder1);
            }else{
                holder2 = (OtherCognitionsAdapter.ViewHolder) convertView.getTag(R.string.holder2);
            }
        }

        if(type == 0){
            final String file = mData.get(position).getPath();
//            String videoUrl = "http://192.168.154.1:8080/file/"+file;
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(videoUrl, new HashMap());
//            Bitmap bitmap = retriever.getFrameAtTime();
//            holder1.img.setImageBitmap(bitmap);
            VedioAsyncTask vedioAsyncTask = new VedioAsyncTask(holder1.img);
            vedioAsyncTask.execute("http://192.168.154.1:8080/file/"+file);

            split = file.split("\\.");
            holder1.text.setText(split[0]);
        }else{
            String file = mData.get(position).getPath();
            split = file.split("\\.");
            holder2.name.setText(split[0]);
            params = new HashMap<String, String>();
            params.put("file",file);
            httpUtil.postRequest("http://192.168.154.1:8080/CognitionAPP/read.do",params,new MyStringCallBack(holder2) {
                @Override
                public void onResponse(String response, int id) {
//                        Log.e("response:",response+" "+id);
                    holder.text.setText(response);
                }
            });
        }
        return convertView;
    }

    public static class ViewHolder{
        ImageView img;
        TextView text;
    }
}
