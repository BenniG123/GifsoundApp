package cpre388.gifsound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GifSoundView extends Activity {

    List<GifSoundViewFragment> fragments = new ArrayList<>();
    List<String> URLs = new ArrayList<>();

    int viewingIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_sound_view);
        Intent intent = getIntent();

        viewingIndex = intent.getIntExtra("ViewingIndex", 0);
        int size = intent.getIntExtra("Size", 0);

        for (int i = 0; i < size; i++) {
            URLs.add( intent.getStringExtra("URL" + i));
            //Log.d("URL", URLs.get(i));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gif_sound_view, menu);
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
}
