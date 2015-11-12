package cpre388.gifsound;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by abjensen on 11/11/2015.
 */
public class GifSoundLink {

    public URL gifSoundLinkURL;
    public URL previewImageURL;
    public Bitmap bitmap;
    public String title;

    public GifSoundLink(URL linkURL, URL imageURL, String gTitle) {
        super();
        gifSoundLinkURL = linkURL;
        previewImageURL = imageURL;
        title = gTitle;
        bitmap = null;
    }

}
