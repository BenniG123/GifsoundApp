package cpre388.gifsound;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class GifSoundViewFragment extends Fragment {
    String link;
    GifSoundMedia media;

    public GifSoundViewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        link = getArguments().getString("GifSoundLink");
        media = GifSoundLinkParser.parseLink(link);

        //TODO set view with objects created

        return inflater.inflate(R.layout.fragment_gif_sound_view, container, false);
    }
}
