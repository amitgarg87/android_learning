package com.lambton.newsreader;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

public class RSSFeedHandler extends DefaultHandler {
    private RSSFeed feed;
    private RSSItem item;
    
    private boolean isAuthor = false;
    private boolean isTitle = false;
    private boolean isGenre = false;
    private boolean isPrice = false;
    private boolean isPubDate = false;
    private boolean isDescription = false;

    public RSSFeed getFeed() {
        return feed;
    }
        
    public void startDocument() throws SAXException {
        feed = new RSSFeed();
        item = new RSSItem();
    }
    
    public void startElement(String namespaceURI, String localName, 
            String qName, Attributes atts) throws SAXException {
        
        if (qName.equals("book")) {
            item = new RSSItem();
            return;
        }
        else if (qName.equals("author")) {
            isAuthor = true;
            return;
        }
        else if (qName.equals("title")) {
            isTitle = true;
            return;
        }
        else if (qName.equals("genre")) {
            isGenre = true;
            return;
        }
        else if (qName.equals("price")) {
            isPrice = true;
            return;
        }
        else if (qName.equals("publish_date")) {
            isPubDate = true;
            return;
        }
        else if (qName.equals("description")) {
            isDescription = true;
            return;
        }

    }
    
    public void endElement(String namespaceURI, String localName, 
            String qName) throws SAXException
    {
        if (qName.equals("book")) {
            feed.addItem(item);
            return;
        }
    }
     
    public void characters(char ch[], int start, int length)
    {
        String s = new String(ch, start, length);
        if (isAuthor) {
            item.setAuthor(s);
            isAuthor = false;
        }
        if (isTitle) {
            item.setTitle(s);
            isTitle = false;
        }
        if (isGenre) {
            item.setGenre(s);
            isGenre = false;
        }
        if (isPrice) {
            item.setPrice(s);
            isPrice = false;
        }
        else if (isPubDate) {
            item.setPubDate(s);
            isPubDate = false;
        }
        else if (isDescription) {
            item.setDescription(s);
            isDescription = false;
        }
    }
}