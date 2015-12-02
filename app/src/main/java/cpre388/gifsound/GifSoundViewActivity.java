package cpre388.gifsound;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class GifSoundViewActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String API_KEY = "AIzaSyBb7IPHhczdj-FAiUN8Yli3_TQj-TDTyZI";
//    public static final String VIDEO_ID = "1HmQNkcAhgg";

    String GifSoundLink;
    String VIDEO_ID, GIF_ID;
    int VIDEO_TIME;

    private YouTubePlayer youTubePlayer;
    private YouTubePlayerFragment youTubePlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_sound_view);

        int viewingIndex = getIntent().getIntExtra("ViewingIndex", 0);
        GifSoundLink = getIntent().getStringExtra("URL" + viewingIndex);
        parseURL(GifSoundLink);

        youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                .findFragmentById(R.id.youtube_fragment);
        youTubePlayerFragment.initialize(API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        this.youTubePlayer = youTubePlayer;

        if (!wasRestored) {
            this.youTubePlayer.loadVideo(VIDEO_ID, VIDEO_TIME*1000);
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.hide(youTubePlayerFragment);
        ft.commit();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    private void parseURL(String link) {
        Toast.makeText(this, link, Toast.LENGTH_LONG).show();
        VIDEO_ID = link.substring(link.indexOf(";v=")+3);
        VIDEO_TIME = Integer.parseInt(VIDEO_ID.substring(VIDEO_ID.indexOf(";s=") + 3));
        VIDEO_ID = VIDEO_ID.substring(0, VIDEO_ID.indexOf("&"));
//        GIF_ID = link.substring(link.indexOf("gif=")+ 4, link.indexOf("&amp"));
    }
}
