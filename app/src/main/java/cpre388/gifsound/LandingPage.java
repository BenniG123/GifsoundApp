package cpre388.gifsound;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LandingPage extends ListActivity implements JSONAsyncTask.ResultHandler{

    JSONAsyncTask.ResultHandler handler;
    GifSoundLinkAdapter adapter;
    ListView linksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

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
        Intent gifsoundIntent = new Intent(Intent.ACTION_VIEW, adapter.data.get(position).gifSoundLinkURL);
        startActivity(browserIntent);
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
}
