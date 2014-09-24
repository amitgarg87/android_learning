package com.lambton.newsreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AmiItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ami_activity_item);
        
        // get references to widgets
        TextView authorView = (TextView) findViewById(R.id.author);
        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView genreView = (TextView) findViewById(R.id.genre);
        TextView priceView = (TextView) findViewById(R.id.price);
        TextView pubDateTextView = (TextView) findViewById(R.id.pubDateTextView);
        TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

        
        // get the intent
        Intent intent = getIntent();
        
        // get data from the intent
        String author = intent.getStringExtra("author"); 
        String title = intent.getStringExtra("title");
        String genre = intent.getStringExtra("genre");
        String price = intent.getStringExtra("price");
        String pubDate = intent.getStringExtra("pubdate"); 
        String description = intent.getStringExtra("description").replace('\n', ' '); 
        
        // display data on the widgets
        authorView.setText(author);
        titleTextView.setText(title);
        genreView.setText(genre);
        priceView.setText(price);
        pubDateTextView.setText(pubDate); 
        descriptionTextView.setText(description); 
        

    }
}