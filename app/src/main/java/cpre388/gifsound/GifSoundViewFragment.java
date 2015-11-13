package cpre388.gifsound;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

/**
 * A placeholder fragment containing a simple view.
 */
public class GifSoundViewFragment extends Fragment {
    String link;
    GifSoundMedia media;

    public static final String API_KEY = "AIzaSyBb7IPHhczdj-FAiUN8Yli3_TQj-TDTyZI";

    public GifSoundViewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       final View view = inflater.inflate(R.layout.fragment_gif_sound_view, container, false);

        link = getArguments().getString("GifSoundLink");
        media = GifSoundLinkParser.parseLink(link);

        YouTubePlayerFragment playerFragment = YouTubePlayerFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, playerFragment).commit();

        playerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    final YouTubePlayer YPlayer = youTubePlayer;
                    YPlayer.setFullscreen(true);
                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    YPlayer.loadVideo("1HmQNkcAhgg", 3000);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("YouTubePlayer", "Initialization Failure");
            }
        });

        //TODO set view with objects created

        return view;
    }
}
