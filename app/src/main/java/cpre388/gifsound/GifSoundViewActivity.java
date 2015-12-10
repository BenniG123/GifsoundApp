package cpre388.gifsound;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

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
    boolean isGfy = false;

    final String testHtml = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "\t<head>\n" +
            "\t\t\n" +
            "\t</head>\n" +
            "\n" +
            "\t<body>\n" +
            "\t\tREPLACE_BODY_CONTENT\n" +
            "\t</body>\n" +
            "\t\n" +
            "\t<script type=\"text/javascript\">\n" +
            "\t\tREPLACE_SCRIPT_CONTENT\n" +
            "\t</script>\n" +
            "</html>";

    final String HTML_VIDEO = "<video id=\"gif\" width=\"100%\" controls autoplay loop muted poster=\"REPLACE_POSTER\" style=\"max-width:800px;max-height:800px;\">\n" +
            "\t\t<source src=\"REPLACE_WEBM\" type=\"video/webm\">\n" +
            "\t\t<source src=\"REPLACE_MP4\" type=\"video/mp4\">\n" +
            "\t\tYour browser does not support HTML5 video.\n" +
            "\t</video>\n";

    final String HTML_GIF = "<img id=\"gif\" width=\"100%\" src=\"REPLACE_GIF\" style=\"max-width:800px;max-height:800px;\">";

    //TODO add listener to call videoReady when readyState equals 4
    final String SCRIPT = "";//"var vid = document.getElementById(\"gif\");\n" + " test.videoReady();";


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

        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    /*Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            while(youTubePlayer == null);

                            while(!youTubePlayer.isPlaying()) {
                                youTubePlayer.play();
                            }
                        }
                    };
                    Handler handler = new Handler();
                    handler.post(runnable);*/
                    try {
                        if (youTubePlayer != null && !youTubePlayer.isPlaying()) {
                            youTubePlayer.play();
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        image.setWebChromeClient(webChromeClient);

        //re-enable autoplay for media
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 16) {
            image.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        //image.getSettings().setJavaScriptEnabled(true);
        //image.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        /*image.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //TODO finished loading webview
                Log.d("finished", "finished");

                //view.evaluateJavascript("document.getElementById(\"gif\").pause();", null);
                //view.evaluateJavascript("document.getElementById(\"gif\").play();", null);
                //youTubePlayer.play();
            }
        });*/
        //image.addJavascriptInterface(new JsObject(), "test");

        image.setInitialScale(100);

        String html_code = writeHTMLCode();
        image.loadDataWithBaseURL("", html_code, "text/html", "UTF-8", "");
    }

    /*class JsObject {
        @JavascriptInterface
        public void videoReady(){
            Log.d("videoStarted", "");
            youTubePlayer.play();
        }
    }*/

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        this.youTubePlayer = youTubePlayer;

        if (!wasRestored) {
            this.youTubePlayer.loadVideo(VIDEO_ID, VIDEO_TIME*1000);
            //this.youTubePlayer.cueVideo(VIDEO_ID, VIDEO_TIME*1000);
        }

        //this.youTubePlayer.pause();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
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
            isGfy = false;
        } else if (link.contains("gif=")) {
            //All other images are sent with gif=website
            GIF_URL = link.substring(link.indexOf("gif=") + 4, link.indexOf("&"));
            isGifv = false;
            isGfy = false;
        } else if(link.contains("gfycat=")){
            GIF_URL = link.substring(link.indexOf("gfycat=")+7, link.indexOf("&"));
            GIF_URL = "http://zippy.gfycat.com/"+ GIF_URL;
            isGfy = true;
            isGifv = false;
        }
        if(!GIF_URL.contains("http")){
            GIF_URL = "http://" + GIF_URL;
        }

        Log.d("GIF_URL", GIF_URL);
    }

    private String writeHTMLCode() {
        String code;


        if (isGifv) {
            //code = HTML_VIDEO;
            code = testHtml;
            code = code.replace("REPLACE_SCRIPT_CONTENT", SCRIPT);
            code = code.replace("REPLACE_BODY_CONTENT", HTML_VIDEO);

            String UrlNoExtensions = GIF_URL.substring(0, GIF_URL.lastIndexOf("."));
            code = code.replace("REPLACE_POSTER", UrlNoExtensions + ".jpg");
            code = code.replace("REPLACE_WEBM", UrlNoExtensions + ".webm");
            code = code.replace("REPLACE_MP4", UrlNoExtensions + ".mp4");
        } else if(isGfy){
            //code = HTML_VIDEO;
            code = testHtml;
            code = code.replace("REPLACE_SCRIPT_CONTENT", SCRIPT);
            code = code.replace("REPLACE_BODY_CONTENT", HTML_VIDEO);

            code = code.replace("REPLACE_POSTER", GIF_URL.replace("zippy", "thumbs") + "-poster.jpg");
            code = code.replace("REPLACE_WEBM", GIF_URL + ".webm");
            code = code.replace("REPLACE_MP4", GIF_URL + ".mp4");
        } else {
            //code = HTML_GIF;
            code = testHtml;
            code = code.replace("REPLACE_SCRIPT_CONTENT", SCRIPT);
            code = code.replace("REPLACE_BODY_CONTENT", HTML_GIF);

            code = code.replace("REPLACE_GIF", GIF_URL);
        }

        return code;
    }
}
