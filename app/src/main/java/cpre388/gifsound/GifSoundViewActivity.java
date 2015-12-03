package cpre388.gifsound;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class GifSoundViewActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String API_KEY = "AIzaSyBb7IPHhczdj-FAiUN8Yli3_TQj-TDTyZI";

    String GifSoundLink;
    String VIDEO_ID, GIF_URL;
    int VIDEO_TIME = 0;

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

        ViewGroup vg = (ViewGroup) findViewById(android.R.id.content);

        vg.addView(new MovieView(this));

        WebView image = (WebView) findViewById(R.id.web_image);
        image.setBackgroundColor(0);
        image.loadUrl(GIF_URL);

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
        VIDEO_ID = link.substring(link.indexOf(";v=") + 3);
        if (VIDEO_ID.contains("&")) {
            VIDEO_TIME = Integer.parseInt(VIDEO_ID.substring(VIDEO_ID.indexOf(";s=")+3));
            VIDEO_ID = VIDEO_ID.substring(0, VIDEO_ID.indexOf("&"));
        }

        if (link.contains("gifv=")) {
            //gifv gifs are encoded with just an id and are from i.imgur.com/ID.gif
            GIF_URL = link.substring(link.indexOf("gifv=")+5, link.indexOf("&"));
            GIF_URL = "http://i.imgur.com/"+ GIF_URL +".gifv";    //GIF_URL = website
        }
        else if (link.contains("gif=")) {
            //All other images are sent with gif=website
            GIF_URL = link.substring(link.indexOf("gif=")+4, link.indexOf("&"));
        }
        Toast.makeText(this, GIF_URL, Toast.LENGTH_LONG).show();

    }
}
