package cpre388.gifsound;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by abjensen on 11/11/2015.
 */
public class GifSoundLinkAdapter extends ArrayAdapter<GifSoundLink> {

    private Context context;
    private int layoutResourceId;
    private List<GifSoundLink> data = null;

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

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            //create a new ItunesHolder and set it to the fields the row in the list view
            holder = new GifSoundHolder();
            
            holder.previewImage = (ImageView) row.findViewById(R.id.previewImage);
            holder.linkTitle = (TextView) row.findViewById(R.id.linkTitle);

            row.setTag(holder);
        } else {
            holder = (GifSoundHolder) row.getTag();
        }

        //get the current position from the list
        GifSoundLink gifSoundLink = data.get(position);


        //  TODO download image from url   holder.previewImage.setText(gifSoundLink);
        holder.linkTitle.setText(gifSoundLink.title);

        return row;
    }

    /**
     * A class to represent the fields in the row layout
     */
    static class GifSoundHolder
    {
        ImageView previewImage;
        TextView linkTitle;
    }
}
