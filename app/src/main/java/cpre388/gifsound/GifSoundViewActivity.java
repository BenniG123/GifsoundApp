package cpre388.gifsound;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
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

    boolean isGifv = false;

    final String HTML = "<video id=\"gif\" autoplay loop muted=\"\" poster=\"REPLACE_POSTER\" style=\"cursor: zoom-in;\">\n" +
            "\t\t<source src=\"REPLACE_WEBM\" type=\"video/webm\">\n" +
            "\t\t<source src=\"REPLACE_MP4\" type=\"video/mp4\">\n" +
            "\t\tYour browser does not support HTML5 video.\n" +
            "\t</video>";

//    final String HTML = "<video autoplay=\"\" loop=\"\" muted=\"\" poster=\"REPLACE_POSTER\" >\n" +
//            "\t\t<source src=\"REPLACE_WEBM\" type=\"video/webm\">\n" +
//            "\t\t<source src=\"REPLACE_MP4\" type=\"video/mp4\">\n" +
//            "\t\tYour browser does not support HTML5 video.\n" +
//            "\t</video>";


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
//        image.setBackgroundColor(0);
        image.setWebChromeClient(new WebChromeClient());

        String html_code = writeHTMLCode();
//        image.loadUrl(GIF_URL);
        image.loadDataWithBaseURL("", html_code, "text/html", "UTF-8", "");
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
        VIDEO_ID = link.substring(link.indexOf(";v=") + 3);
        if (VIDEO_ID.contains("&")) {
            VIDEO_TIME = Integer.parseInt(VIDEO_ID.substring(VIDEO_ID.indexOf(";s=")+3));
            VIDEO_ID = VIDEO_ID.substring(0, VIDEO_ID.indexOf("&"));
        }

        if (link.contains("gifv=")) {
            //gifv gifs are encoded with just an id and are from i.imgur.com/ID.gif
            GIF_URL = link.substring(link.indexOf("gifv=")+5, link.indexOf("&"));
            GIF_URL = "http://i.imgur.com/"+ GIF_URL +".gifv";    //GIF_URL = website
            isGifv = true;
        }
        else if (link.contains("gif=")) {
            //All other images are sent with gif=website
            GIF_URL = link.substring(link.indexOf("gif=")+4, link.indexOf("&"));
        }

    }

    private String writeHTMLCode() {
        String code = HTML;
//        if (isGifv) {
            String UrlNoExtensions = GIF_URL.substring(0, GIF_URL.lastIndexOf("."));
            code = code.replace("REPLACE_POSTER", UrlNoExtensions + ".jpg");
            code = code.replace("REPLACE_WEBM", UrlNoExtensions + ".webm");
            code = code.replace("REPLACE_MP4", UrlNoExtensions + ".mp4");
            Log.d("HTML", code);
//        }
        return code;
    }
}
