package cpre388.gifsound;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LandingPage extends Activity {

    DownloadWebpageTask.ResultHandler handler;

    GifSoundLinkAdapter adapter;

    ListView linksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        String query = "https://www.reddit.com/r/gifsound/.json?sort=hot";

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
        //TODO Handle the Result of a Network Call
        try {
            JSONObject o = new JSONObject(result);
            JSONArray jsonArray = o.getJSONArray("results");

            List<GifSoundLink> list = new ArrayList<GifSoundLink>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                String title = obj.getString("collectionName");
                String album = obj.getString("trackName");

                list.add(new GifSoundLink(album, title));
            }

            adapter = new GifSoundLinkAdapter(this, R.layout.gifsoundlinklayout, list);

            linksListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Exception", "Request not completed");
        }


    }
}
