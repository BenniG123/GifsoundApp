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
    public String id;
    public String name;

    public GifSoundLink(URL linkURL, URL imageURL, String gTitle, String id, String name) {
        gifSoundLinkURL = linkURL;
        previewImageURL = imageURL;
        title = gTitle;
        bitmap = null;
        this.id = id;
        this.name = name;
    }

}
