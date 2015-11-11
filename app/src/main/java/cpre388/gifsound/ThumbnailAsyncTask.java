package cpre388.gifsound;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abjensen on 11/11/2015.
 */
public class ThumbnailAsyncTask extends AsyncTask<URL, Integer, Bitmap> {
    ImageView imageView;

    public ThumbnailAsyncTask(ImageView i){
        imageView = i;
    }
    @Override
    protected Bitmap doInBackground(URL... params) {
        Bitmap image = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) params[0].openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            image = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return image;
    }

    @Override
    protected void onProgressUpdate(Integer... progress){
        //This is not implemented here, but know that you can use this to notify users of the task's progress
    }

    protected void onPostExecute(Bitmap result){
        imageView.setImageBitmap(result);
    }
}
