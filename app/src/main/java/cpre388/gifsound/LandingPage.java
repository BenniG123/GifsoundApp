package cpre388.gifsound;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LandingPage extends ListActivity implements JSONAsyncTask.ResultHandler{
    private SwipeRefreshLayout swipeContainer;

    JSONAsyncTask.ResultHandler handler;
    GifSoundLinkAdapter adapter;
    ListView linksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //TODO code to call when refreshing page
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        handler = this;
        linksListView = (ListView) findViewById(android.R.id.list);

//        linksListView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adapter.getItem()
//            }
//        });

        /* Button search = (Button)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String artist = null;
                TextView usernameView = (TextView) findViewById(R.id.username);
                artist = usernameView.getText().toString();
                Log.e("Artist Searched", artist);

                new JSONAsyncTask(handler).execute("https://itunes.apple.com/search?term=" + artist.toLowerCase().replace(' ', '+') + "&entity=song&limit=20");
            }
        }); */

        String query = "https://www.reddit.com/r/gifsound/.json?sort=hot";
        new JSONAsyncTask(handler).execute(query);
    }

    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent gifsoundIntent = new Intent(this, GifSoundView.class);

        for (int i = 0; i < adapter.data.size(); i++) {
            gifsoundIntent.putExtra("URL" + i, adapter.data.get(i).gifSoundLinkURL.toString());
        }

        gifsoundIntent.putExtra("ViewingIndex", position);
        gifsoundIntent.putExtra("Size", adapter.data.size());
        startActivity(gifsoundIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleResult(String result) {
        //Handle the Result of a Network Call
        try {
            JSONObject o = new JSONObject(result);
            JSONObject data = o.getJSONObject("data");
            JSONArray jsonArray = data.getJSONArray("children");

            List<GifSoundLink> list = new ArrayList<GifSoundLink>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONObject childData = obj.getJSONObject("data");

                URL thumbnail = null;
                try {
                    thumbnail = new URL(childData.getString("thumbnail"));
                    Log.d("thumbnail", thumbnail.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    if (thumbnail == null) {

                    }
                }

                URL link = null;
                try {
                    link = new URL(childData.getString("url"));
                    Log.d("link", link.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    if (link == null) {

                    }
                }

                String title = childData.getString("title");
                list.add(new GifSoundLink(link, thumbnail, title));
            }

            adapter = new GifSoundLinkAdapter(this, R.layout.gifsoundlinklayout, list);

            linksListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Exception", "Request not completed");
        }


    }

    public void seeLinkComments(View v){
        //get the row the clicked button is in
        LinearLayout rowButtonClicked = (LinearLayout)v.getParent();

        //TODO find out what row rowButtonClicked was in the list, and open a page to view the
        //TODO corresponding link's comments from reddit

        rowButtonClicked.refreshDrawableState();
    }
}
