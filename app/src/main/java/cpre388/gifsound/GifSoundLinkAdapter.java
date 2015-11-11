package cpre388.gifsound;

import android.content.Context;
import android.widget.ArrayAdapter;

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
}
