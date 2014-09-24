package com.lambton.newsreader;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AmiItemsActivity extends Activity 
implements OnItemClickListener {

    private RSSFeed feed;
    private FileIO io;
    
    private TextView titleTextView;
    private ListView itemsListView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ami_activity_items);
        
        io = new FileIO(getApplicationContext());
        
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        itemsListView = (ListView) findViewById(R.id.itemsListView);
        
        itemsListView.setOnItemClickListener(this);
        
        new DownloadFeed().execute();
    }
    
    class DownloadFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            io.downloadFile();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            Log.d("Books reader", "XML downloaded");
            new ReadFeed().execute();
        }
    }
    
    class ReadFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            feed = io.readFile();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            Log.d("Books reader", "Feed read");
            
            // update the display for the activity
            AmiItemsActivity.this.updateDisplay();
        }
    }
    
    public void updateDisplay()
    {
        if (feed == null) {
            titleTextView.setText("Unable to get XML file");
            return;
        }

      
        // get the items for the feed
        ArrayList<RSSItem> items = feed.getAllItems();

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data = 
                new ArrayList<HashMap<String, String>>();
        for (RSSItem item : items) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("price", item.getPrice());
            map.put("description", item.getDescription());
            data.add(map);
        }
        
        // create the resource, from, and to variables 
        int resource = R.layout.ami_listview_item;
        String[] from = {"price", "description"};
        int[] to = {R.id.titleTextView, R.id.author};

        // create and set the adapter
        SimpleAdapter adapter = 
            new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
        
        Log.d("Books reader", "Books displayed");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, 
            int position, long id) {

        // get the item at the specified position
        RSSItem item = feed.getItem(position);

        // create an intent
        Intent intent = new Intent(this, AmiItemActivity.class);
        intent.putExtra("author", item.getAuthor());
        intent.putExtra("title", item.getTitle());
        intent.putExtra("genre", item.getGenre());
        intent.putExtra("price", item.getPrice());
        intent.putExtra("pubdate", item.getPubDate());
        intent.putExtra("description", item.getDescription());

        this.startActivity(intent);
    }
}