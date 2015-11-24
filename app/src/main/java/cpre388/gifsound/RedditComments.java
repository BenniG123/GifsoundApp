package cpre388.gifsound;

import android.os.Bundle;
import android.app.Activity;

public class RedditComments extends Activity {
    String redditLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddit_comments);

        redditLink = getIntent().getStringExtra("redditLinkName");

        //TODO fetch comments from reddit for the page "redditLink"
        //TODO figure out how to display the comments in the xml
    }

}
