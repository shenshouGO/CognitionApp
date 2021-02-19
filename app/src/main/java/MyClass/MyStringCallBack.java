package MyClass;

import com.example.administrator.myapplication2.Adapter.MixedAdapter;
import com.example.administrator.myapplication2.Adapter.OtherCognitionsAdapter;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class MyStringCallBack extends StringCallback {

    public OtherCognitionsAdapter.ViewHolder holder;
    public MixedAdapter.ViewHolder1 holder1;

    public MyStringCallBack(){

    }

    public MyStringCallBack(OtherCognitionsAdapter.ViewHolder holder){
        this.holder = holder;
    }

    public MyStringCallBack(MixedAdapter.ViewHolder1 holder){
        this.holder1 = holder;
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(String response, int id) {

    }

}
