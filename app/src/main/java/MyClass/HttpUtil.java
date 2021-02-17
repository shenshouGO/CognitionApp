package MyClass;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.lzy.imagepicker.bean.ImageItem;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    private PostFormBuilder mPost;
    private GetBuilder mGet;
    private OkHttpClient okHttpClient;

    public HttpUtil() {
        OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .writeTimeout(15 * 1000L, TimeUnit.MILLISECONDS)
                .build();

        mPost = OkHttpUtils.post();
        mGet = OkHttpUtils.get();
        okHttpClient = new OkHttpClient();
    }

    //封装请求
    public void postRequest(String url, Map<String, String> params, MyStringCallBack callback) {
        mPost.url(url)
                .params(params)
                .build()
                .execute(callback);
    }

    //上传文件
    public void postFileRequest(String url, Map<String, String> params, ArrayList<ImageItem> pathList, MyStringCallBack callback) {

        Map<String,File> files = new HashMap<>();
        for (int i = 0; i < pathList.size(); i++) {
            String newPath = BitmapUtils.compressImageUpload(pathList.get(i).path);
            files.put(pathList.get(i).name,new File(newPath));
        }

        for (Map.Entry<String, File> entry: files.entrySet()) {
            Log.e("files:", entry.getKey()+"   "+entry.getValue());
        }

        for (Map.Entry<String, String> entry: params.entrySet()) {
            Log.e("param:", entry.getKey()+"   "+entry.getValue());
//            mPost.addParams(entry.getKey(),entry.getValue());
        }

        mPost.url(url)
                .files("files",files)
                .build()
                .execute(callback);
    }

    public void download(final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
