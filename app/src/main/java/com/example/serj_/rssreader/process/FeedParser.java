package com.example.serj_.rssreader.process;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.models.Item;
import lombok.Getter;
import lombok.NonNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;


public final class FeedParser {
    private final XmlPullParser parser;
    private enum TypeOfFeed {RSS, ATOM, ELSE}
    private final Tags tags;
    private TypeOfFeed feedType;
    private int channelID;
    private static final Logger logger = Logger.getLogger("MyLogger");

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

    private  void getTypeOfChannel() throws XmlPullParserException, IOException{

        parser.next();
        final String type =  parser.getName();
        parser.next();
        logger.info(type);

        switch (type) {
            case "rss": {
                logger.info("Yep, that's rss");
                this.feedType = TypeOfFeed.RSS;
                break;
            }
            case "atom": {
                logger.info("Looks like atom");
                this.feedType = TypeOfFeed.ATOM;
                break;
            }
            default:{
                logger.info("That's something else");
                this.feedType = TypeOfFeed.ELSE;
                break;
            }
        }
    }

    public ArrayList<Item> getListOfItems() throws XmlPullParserException,IOException{
        ArrayList<Item> outputList = new ArrayList<>();
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
    private Item getItem()throws XmlPullParserException,IOException{
        Item item = new Item();
        String nameTag;
        String text;
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
                    logger.info(nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getLink())){
                    text = parseTag(nameTag);
                    item.setLinkOfPost(text);
                    logger.info(nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getDescription())){
                    text = parseTag(nameTag);
                    item.setDescriptionOfPost(text);
                    logger.info(nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getPubDate())){
                    text = parseTag(nameTag);
                    item.setDateOfPost(text);
                    logger.info(nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getPubDate())){
                    text = parseTag(nameTag);
                    item.setDateOfPost(text);
                    logger.info(nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getGuid())){
                    text = parseTag(nameTag);
                    item.setIdOfPost(text);
                    logger.info(nameTag + ": " + text);
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
                    logger.info(nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getLink())){
                    text = parseTag(nameTag);
                    chan.setChannelLink(text);
                    logger.info(nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getDescription())){
                    text = parseTag(nameTag);
                    chan.setDescription(text);
                    logger.info(nameTag + ": " + text);
                }
                else if(nameTag.equals(tags.getPubDate())){
                    text = parseTag(nameTag);
                    chan.setLastUpdate(text);
                    logger.info(nameTag + ": " + text);
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
    private String parseTag(String nameTag)throws XmlPullParserException,IOException{
        String textOfTag = "";
        while(!((parser.getEventType()==XmlPullParser.END_TAG)&&(parser.getName().equals(nameTag)))) {
            if(parser.getText()!=null) {
                textOfTag += parser.getText();
            }
            parser.nextToken();
        }
        return textOfTag;
    }
}
