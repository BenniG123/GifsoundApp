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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LandingPage extends ListActivity implements JSONAsyncTask.ResultHandler{
    enum sortStyle{
        HOT, NEW, RISING, CONTROVERSIAL, TOP_HOUR, TOP_24_HOURS, TOP_WEEK, TOP_MONTH, TOP_YEAR, TOP_ALL_TIME
    }

    JSONAsyncTask.ResultHandler handler;
    GifSoundLinkAdapter adapter;

    ListView linksListView;
    SwipeRefreshLayout swipeContainer;

    //TODO change this variable based on the selected sorting style
    sortStyle currentSortingStyle = sortStyle.HOT;
    int postLoadCount = 25;

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
                if(adapter != null){
                    adapter.clear();
                    adapter.data.clear();
                    linksListView.setOnScrollListener(new EndlessScrollListener());
                    try {
                        new JSONAsyncTask(handler).execute(generateFetchURL()).get(10000, TimeUnit.MILLISECONDS);
                    } catch(TimeoutException e) {
                        //TODO eventually print toast that refresh timed-out
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        //not sure what causes this exception
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        //not sure what causes this exception
                        e.printStackTrace();
                    } finally {
                        adapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                }
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

        //uses inner class to call to reddit for more posts when at bottom of the listview
        linksListView.setOnScrollListener(new EndlessScrollListener());

        String query = generateFetchURL();
        new JSONAsyncTask(handler).execute(query);
    }

    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent gifsoundIntent = new Intent(this, GifSoundViewActivity.class);

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
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    if (thumbnail == null) {

                    }
                }

                URL link = null;
                try {
                    link = new URL(childData.getString("url"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    if (link == null) {

                    }
                }

                String name = childData.getString("id");

                String title = childData.getString("title");
                list.add(new GifSoundLink(link, thumbnail, title, name));
            }

            if(adapter == null) {
                adapter = new GifSoundLinkAdapter(this, R.layout.gifsoundlinklayout, list);
                linksListView.setAdapter(adapter);
            }else
               adapter.data.addAll(list);

            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Exception", "Request not completed");
        }


    }

    public void seeLinkComments(View v){
        //get the row the clicked button is in
        LinearLayout rowButtonClicked = (LinearLayout)v.getParent();

        //TODO uncomment button from gifsoundlinklayout.xml once functionality to press button and listview row both work

        //TODO find out what row rowButtonClicked was in the list, and open a page to view the
        //TODO corresponding link's comments from reddit

        rowButtonClicked.refreshDrawableState();
    }

    //This class allows us to keep loading content when we get to the bottom of the currently loaded content
    public class EndlessScrollListener implements AbsListView.OnScrollListener {
        /*
        visibleThreshold – The minimum amount of items to have below your current scroll position, before loading more.
        currentPage – The current page of data you have loaded
        previousTotal – The total number of items in the dataset after the last load
        loading – True if we are still waiting for the last set of data to load.
         */

        private int visibleThreshold = 5;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }
        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                String finalURL = generateFetchURL();
                new JSONAsyncTask(handler).execute(finalURL);

                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    //returns based on the current sorting style the next 25 posts from /r/gifsound
    public String generateFetchURL(){
        String finalURL = "https://www.reddit.com/r/gifsound/";

        switch(currentSortingStyle){
            case HOT:
                finalURL += "hot/.json?sort=hot";
                break;
            case NEW:
                finalURL += "new/.json?sort=new";
                break;
            case RISING:
                finalURL += "rising/.json?sort=rising";
                break;
            case CONTROVERSIAL:
                finalURL += "controversial/.json?sort=controversial";
                break;
            case TOP_HOUR:
                finalURL += "top/.json?sort=top&t=hour";
                break;
            case TOP_24_HOURS:
                finalURL += "top/.json?sort=top&t=day";
                break;
            case TOP_WEEK:
                finalURL += "top/.json?sort=top&t=week";
                break;
            case TOP_MONTH:
                finalURL += "top/.json?sort=top&t=month";
                break;
            case TOP_YEAR:
                finalURL += "top/.json?sort=top&t=year";
                break;
            case TOP_ALL_TIME:
                finalURL += "top/.json?sort=top&t=all";
                break;
        }

        if(adapter == null || adapter.data.size() == 0){
            //nothing for now
        } else {
            finalURL += "&count=" + (adapter.data.size()) + "&after=" + (adapter.data.get(adapter.data.size() - 1).redditLink);
        }

        return finalURL;
    }
}
