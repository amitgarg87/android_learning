package com.lambton.newsreader;

import java.util.ArrayList;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class RSSFeed {

    private ArrayList<RSSItem> items;
        
    public RSSFeed() {
        items = new ArrayList<RSSItem>(); 
    }
    
   
    public int addItem(RSSItem item) {
        items.add(item);
        return items.size();
    }
    
    public RSSItem getItem(int index) {
        return items.get(index);
    }
    
    public ArrayList<RSSItem> getAllItems() {
        return items;
    }    
}