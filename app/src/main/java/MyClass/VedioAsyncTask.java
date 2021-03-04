package MyClass;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by Administrator on 2021/3/1.
 */

public class VedioAsyncTask extends AsyncTask<String,Void,Bitmap> {
    private ImageView mImageView;

    public VedioAsyncTask(ImageView mImageView) {
        super();
        this.mImageView = mImageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(params[0], new HashMap());
        Bitmap bitmap = retriever.getFrameAtTime();
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mImageView.setImageBitmap(bitmap);
        this.cancel(true);
    }
}
