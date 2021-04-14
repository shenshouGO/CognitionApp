package MyClass;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.myapplication2.Adapter.HotCognitionAdapter;
import com.example.administrator.myapplication2.Adapter.MixedAdapter;
import com.example.administrator.myapplication2.Adapter.OtherCognitionsAdapter;
import com.example.administrator.myapplication2.Adapter.ScreenAdapter;
import com.example.administrator.myapplication2.R;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class MyStringCallBack extends StringCallback {

    public OtherCognitionsAdapter.ViewHolder holder;
    public MixedAdapter.ViewHolder1 holder1;
    public HotCognitionAdapter.ViewHolder holder2;
    public ScreenAdapter.ViewHolder screenAdapter_holder;

    public ImageView v;
    public int position;
    private Context mContext;

    public MyStringCallBack(){

    }

    public MyStringCallBack(OtherCognitionsAdapter.ViewHolder holder){
        this.holder = holder;
    }

    public MyStringCallBack(MixedAdapter.ViewHolder1 holder){
        this.holder1 = holder;
    }

    public MyStringCallBack(int position, HotCognitionAdapter.ViewHolder holder){
        this.position = position;
        this.holder2 = holder;
    }

    public MyStringCallBack(ScreenAdapter.ViewHolder holder){
        this.screenAdapter_holder = holder;
    }

    public MyStringCallBack(Context mContext,ImageView v){
        this.v = (ImageView)v;
        this.mContext = mContext;
    }

    public void setGoodID(String s){
        switch (s){
            case "Delete unsuccessfully":
                Toast.makeText(mContext,"取消点赞失败",Toast.LENGTH_SHORT).show();
                break;
            case "Delete successfully":
                v.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.disgood));
                Toast.makeText(mContext,"取消点赞成功",Toast.LENGTH_SHORT).show();
                v.setTag(R.string.goodID,"0");
                break;
            case "Give v unsuccessfully":
                Toast.makeText(mContext,"点赞失败",Toast.LENGTH_SHORT).show();
                v.setTag(R.string.goodID,"0");
                break;
            default:
                v.setTag(R.string.goodID,s);
                v.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.isgood));
                Toast.makeText(mContext,"点赞成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(String response, int id) {

    }

}
