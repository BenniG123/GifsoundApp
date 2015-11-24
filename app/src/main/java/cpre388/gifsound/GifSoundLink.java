package cpre388.gifsound;

import android.graphics.Bitmap;
import java.io.Serializable;
import java.net.URL;

/**
 * Created by abjensen on 11/11/2015.
 */
public class GifSoundLink {

    public URL gifSoundLinkURL;
    public URL previewImageURL;
    public Bitmap bitmap;
    public String title;
    public String redditLink;

    public GifSoundLink(URL linkURL, URL imageURL, String gTitle, String link) {
        gifSoundLinkURL = linkURL;
        previewImageURL = imageURL;
        title = gTitle;
        bitmap = null;
        redditLink = link;
    }

}
