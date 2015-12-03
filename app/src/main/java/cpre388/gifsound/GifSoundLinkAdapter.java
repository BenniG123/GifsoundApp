package cpre388.gifsound;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by abjensen on 11/11/2015.
 */
public class GifSoundLinkAdapter extends ArrayAdapter<GifSoundLink> {

    private Context context;
    private int layoutResourceId;
    public List<GifSoundLink> data = null;

    public GifSoundLinkAdapter(Context context, int layoutResourceId, List<GifSoundLink> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    /* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GifSoundHolder holder;

        //get the current position from the list
        final GifSoundLink gifSoundLink = data.get(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            //create a new GifSoundHolder and set it to the fields the row in the list view
            holder = new GifSoundHolder();

            holder.previewImage = (ImageView) row.findViewById(R.id.previewImage);
            holder.linkTitle = (TextView) row.findViewById(R.id.linkTitle);
            holder.commentsButton = (Button) row.findViewById((R.id.viewRedditCommentsButton));

            row.setTag(holder);

        } else {
            holder = (GifSoundHolder) row.getTag();
        }

        if (gifSoundLink.bitmap == null) {
            ThumbnailAsyncTask a = new ThumbnailAsyncTask();
            try {
                gifSoundLink.bitmap = a.execute(gifSoundLink.previewImageURL).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        holder.previewImage.setImageBitmap(gifSoundLink.bitmap);
        holder.linkTitle.setText(gifSoundLink.title);

        holder.commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent commentsIntent = new Intent(context, RedditComments.class);
//
//                commentsIntent.putExtra("redditLinkName", gifSoundLink.redditLink);
//                context.startActivity(commentsIntent);

                Intent openAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://redd.it/" + gifSoundLink.redditLink));
                Intent chooseBrowserIntent = Intent.createChooser(openAppIntent, "Choose your browser");
                context.startActivity(chooseBrowserIntent);
            }
        });

        return row;
    }

    /**
     * A class to represent the fields in the row layout
     */
    static class GifSoundHolder
    {
        ImageView previewImage;
        TextView linkTitle;
        TextView postAuthorDate;
        Button commentsButton;
    }
}
