package com.example.administrator.myapplication2.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication2.Bean.Comment;
import com.example.administrator.myapplication2.R;

import java.util.List;

/**
 * Created by Administrator on 2021/2/9.
 */

public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<Comment> mData = null;
    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_REPLY = 1;
    private String s;
    private SpannableString spannableString;

    public CommentAdapter(Context mContext,List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<Comment> mData){
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
        if (mData.get(position).getC_c_id().equals("0")) {
            return TYPE_COMMENT;
        } else{
            return TYPE_REPLY;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.answer,parent,false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.answer);
//            holder.comment = (TextView) convertView.findViewById(R.id.answer);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        switch (type){
            case TYPE_COMMENT:
                final String u_name = mData.get(position).getU_name();
                s = u_name+"："+mData.get(position).getComment();
                spannableString=new SpannableString(s);
                final int start1 = s.indexOf(u_name);
                spannableString.setSpan(new ClickableSpan(){
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(mContext,u_name,Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                },start1,start1+u_name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                break;

            case TYPE_REPLY:
                final String u_name2 = mData.get(position).getU_name();
                final String r_u_name = mData.get(position).getR_u_name();
                s = u_name2+" 回复 ";
                final int start2 = 0;
                final int start3 = s.length();
                s = s+r_u_name+"："+mData.get(position).getComment();
                spannableString=new SpannableString(s);

                spannableString.setSpan(new ClickableSpan(){
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(mContext,u_name2,Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                },start2,start2+u_name2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ClickableSpan(){
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(mContext,r_u_name,Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                },start3,start3+r_u_name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                Log.e("Comment","u_name = "+u_name2+"   r_u_name = "+r_u_name+" "+start2+" "+start3);
                break;
        }
        holder.name.setMovementMethod(LinkMovementMethod.getInstance());
        holder.name.setText(spannableString);
//        holder.comment.setText(mData.get(position).getComment());
        return convertView;
    }

    static class ViewHolder{
        TextView name;
//        TextView comment;
    }
}
