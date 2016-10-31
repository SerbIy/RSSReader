package com.example.serj_.rssreader.process;
import android.util.Log;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.models.FeedItem;
import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.NonNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by serj_ on 19.10.2016.
 */
public final class FeedParser {
    private final XmlPullParser parser;
    private enum TypeOfFeed {RSS, ATOM, ELSE};
    private final Tags tags;
    private TypeOfFeed feedType;
    private int channelID;

    @Getter
    private final class Tags {
        final private String channel;
        final private String item;
        final private String title;
        final private String link;
        final private String description;
        final private String pubDate;
        final private String content;
        final private String guid;
        private Tags() {
            switch(feedType) {
                case RSS: {
                    this.channel = "channel";
                    this.title = "title";
                    this.link = "link";
                    this.description = "description";
                    this.pubDate = "pubDate";
                    this.item = "item";
                    this.content = "url";
                    this.guid = "guid";
                    break;
                }
                case ATOM:{
                    this.channel = "channel";
                    this.title = "title";
                    this.link = "link";
                    this.description = "description";
                    this.pubDate = "pubDate";
                    this.item = "item";
                    this.content = "url";
                    this.guid = "guid";
                    break;
                }
                default:{
                    this.channel = "";
                    this.title = "";
                    this.link = "";
                    this.description = "";
                    this.pubDate = "";
                    this.item = "";
                    this.content = "";
                    this.guid = "";
                    break;
                }
            }
        }
    }


    public FeedParser(@NonNull final InputStream stream)throws IOException,XmlPullParserException{

        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        this.parser = parserFactory.newPullParser();
        parser.setInput(stream,"UTF-8");
        this.getTypeOfChannel();
        this.tags = new Tags();
    }

    void getTypeOfChannel() throws XmlPullParserException, IOException{

        parser.next();
        final String type =  parser.getName();
        parser.next();
       Log.i ("Tag",type);
        if (type.equals("rss")){
            Log.i ("Tag","Yep, that's rss");
            this.feedType = TypeOfFeed.RSS;
        }
        else if(type.equals("atom"))
        {
            Log.i ("Tag","Looks like atom");
            this.feedType = TypeOfFeed.ATOM;

        }
        else{
            Log.i ("Tag","That's something else");
            this.feedType = TypeOfFeed.ELSE;
        }
    }

    public ArrayList<FeedItem> getListOfItems() throws XmlPullParserException,IOException{
        ArrayList<FeedItem> outputList = new ArrayList<FeedItem>();
        while(!(((parser.getEventType()==XmlPullParser.END_TAG)&&(parser.getName().equals(tags.getChannel())))||(parser.getEventType()==XmlPullParser.END_DOCUMENT))){
            if((parser.getEventType()==XmlPullParser.START_TAG)&&(parser.getName().equals(tags.item))) {
                outputList.add(getItem());
            }
            else {
                parser.nextTag();
            }
        }
        return(outputList);
    }
    public FeedItem getItem()throws XmlPullParserException,IOException{
        Log.i("TagInItem","Check");
        FeedItem item = new FeedItem();
        String nameTag;
        String text;
        int i = 0;
        while(!((parser.getEventType()==XmlPullParser.START_TAG)&&(parser.getName().equals(tags.item)))) {
            parser.next();
        }
        parser.next();
        while(!((parser.getEventType()==XmlPullParser.END_TAG)&&(parser.getName().equals(tags.item)))){

            if(parser.getEventType()==XmlPullParser.START_TAG) {
                nameTag = parser.getName();

                if(nameTag.equals(tags.getTitle())){
                    text = parseTag(nameTag);
                    item.setTitleOfPost(text);
                    Log.i("TextInItem", nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getLink())){
                    text = parseTag(nameTag);
                    item.setLinkOfPost(text);
                    Log.i("TextInItem", nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getDescription())){
                    text = parseTag(nameTag);
                    item.setDescriptionOfPost(text);
                    Log.i("TextInItem", nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getPubDate())){
                    text = parseTag(nameTag);
                    item.setDateOfPost(text);
                    Log.i("TextInItem", nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getPubDate())){
                    text = parseTag(nameTag);
                    item.setDateOfPost(text);
                    Log.i("TextInItem", nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getGuid())){
                    text = parseTag(nameTag);
                    item.setGuidOfPost(text);
                    Log.i("TextInItem", nameTag + ": " + text);
                }
                else if(!nameTag.equals(tags.getChannel())){
                    parseTag(nameTag);
                }
            }
            parser.next();

        }
        item.setChannelID(channelID);
        return (item);
    }

    public Channel getChannelInfo() throws XmlPullParserException, IOException{
        getTypeOfChannel();
        Channel chan = new Channel();
        String text;
        String nameTag;
        while (!isParsingOfChannelOver()){
            if(parser.getEventType()==XmlPullParser.START_TAG) {
                nameTag = parser.getName();

                if(nameTag.equals(tags.getTitle())){
                    text = parseTag(nameTag);
                    chan.setChannelName(text);
                    Log.i("TextInTag", nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getLink())){
                    text = parseTag(nameTag);
                    chan.setChannelLink(text);
                    Log.i("TextInTag", nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getDescription())){
                    text = parseTag(nameTag);
                    chan.setDescription(text);
                    Log.i("TextInTag", nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getPubDate())){
                    text = parseTag(nameTag);
                    chan.setLastUpdate(text);
                    Log.i("TextInTag", nameTag + ": " + text);
                }

                else if(!nameTag.equals(tags.getChannel())){
                    parseTag(nameTag);
                }
            }
                    parser.next();

        }
        this.channelID = chan.hashCode();
        chan.setChannelID(chan.hashCode());
        return (chan);
    }

    private boolean isParsingOfChannelOver() throws XmlPullParserException {
        return ((parser.getEventType()==XmlPullParser.START_TAG)&&(parser.getName().equals(tags.item)))
                ||((parser.getEventType()==XmlPullParser.END_TAG)&&(parser.getName().equals(tags.channel)));
    }
    private String parseTag(@NotNull String nameTag)throws XmlPullParserException,IOException{
        String textOfTag = "";
        while(!((parser.getEventType()==XmlPullParser.END_TAG)&&(parser.getName().equals(nameTag)))) {
            if(parser.getText()!=null) {
                textOfTag += parser.getText();
            }
            parser.next();
        }
        return textOfTag;
    }
}
