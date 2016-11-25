package com.example.serj_.rssreader.process;
import com.example.serj_.rssreader.model.Channel;
import com.example.serj_.rssreader.model.Item;
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

    private int channelID;
    private XmlPullParser parser = null;
    private enum TypeOfFeed {RSS, ATOM, ELSE}
    private Tags tags = null;
    private TypeOfFeed feedType;

    private static final Logger logger = Logger.getLogger(FeedParser.class.getName());

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


    public FeedParser(@NonNull final InputStream stream){
        try {
            final XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            this.parser = parserFactory.newPullParser();
            parser.setInput(stream, "UTF-8");
            this.getTypeOfChannel();
            this.tags = new Tags();
        }
        catch (final XmlPullParserException exception){
            logger.warning("Can't parse");
        }
    }

    private  void getTypeOfChannel(){
        try {
            parser.next();
            final String type = parser.getName();
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
                default: {
                    logger.info("That's something else");
                    this.feedType = TypeOfFeed.ELSE;
                    break;
                }
            }
        }
        catch (final XmlPullParserException|IOException exception){
            logger.warning("Can't get type of channel");
        }
    }

    public ArrayList<Item> getListOfItems(){
        ArrayList<Item> outputList = new ArrayList<>();
        try {
            while (!(((parser.getEventType() == XmlPullParser.END_TAG) && (parser.getName().equals(tags.getChannel()))) || (parser.getEventType() == XmlPullParser.END_DOCUMENT))) {
                if ((parser.getEventType() == XmlPullParser.START_TAG) && (parser.getName().equals(tags.item))) {
                    outputList.add(getItem());
                } else {
                    parser.nextTag();
                }
            }
        }
        catch (final XmlPullParserException|IOException exception){
            logger.warning("Can't get list of items");
        }
        return(outputList);
    }

    private Item getItem(){
        Item item = new Item();
        try {
            String nameTag;
            String text;
            while (!((parser.getEventType() == XmlPullParser.START_TAG) && (parser.getName().equals(tags.item)))) {
                parser.next();
            }
            parser.next();
            while (!((parser.getEventType() == XmlPullParser.END_TAG) && (parser.getName().equals(tags.item)))) {

                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    nameTag = parser.getName();

                    if (nameTag.equals(tags.getTitle())) {
                        text = parseTag(nameTag);
                        item.setTitleOfPost(text);
                        logger.info(nameTag + ": " + text);
                    } else if (nameTag.equals(tags.getLink())) {
                        text = parseTag(nameTag);
                        item.setLinkOfPost(text);
                        logger.info(nameTag + ": " + text);
                    } else if (nameTag.equals(tags.getDescription())) {
                        text = parseTag(nameTag);
                        item.setDescriptionOfPost(text);
                        logger.info(nameTag + ": " + text);
                    } else if (nameTag.equals(tags.getPubDate())) {
                        text = parseTag(nameTag);
                        item.setDateOfPost(text);
                        logger.info(nameTag + ": " + text);
                    } else if (nameTag.equals(tags.getPubDate())) {
                        text = parseTag(nameTag);
                        item.setDateOfPost(text);
                        logger.info(nameTag + ": " + text);
                    } else if (nameTag.equals(tags.getGuid())) {
                        text = parseTag(nameTag);
                        item.setIdOfPost(text);
                        logger.info(nameTag + ": " + text);
                    } else if (!nameTag.equals(tags.getChannel())) {
                        parseTag(nameTag);
                    }
                }
                parser.next();

            }
            item.setChannelID(channelID);
        }
        catch (final XmlPullParserException|IOException exception){
            logger.warning("Can't get item");
        }
            return (item);
    }

    public Channel getChannelInfo(){
        Channel chan = new Channel();
        try {
            getTypeOfChannel();
            String text;
            String nameTag;
            while (!isParsingOfChannelOver()) {
                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    nameTag = parser.getName();

                    if (nameTag.equals(tags.getTitle())) {
                        text = parseTag(nameTag);
                        chan.setChannelName(text);
                        logger.info(nameTag + ": " + text);
                    } else if (nameTag.equals(tags.getLink())) {
                        text = parseTag(nameTag);
                        chan.setChannelLink(text);
                        logger.info(nameTag + ": " + text);
                    } else if (nameTag.equals(tags.getDescription())) {
                        text = parseTag(nameTag);
                        chan.setDescription(text);
                        logger.info(nameTag + ": " + text);
                    } else if (nameTag.equals(tags.getPubDate())) {
                        text = parseTag(nameTag);
                        chan.setLastUpdate(text);
                        logger.info(nameTag + ": " + text);
                    } else if (!nameTag.equals(tags.getChannel())) {
                        parseTag(nameTag);
                    }
                }
                parser.next();

            }
            this.channelID = chan.hashCode();
            chan.setChannelID(chan.hashCode());
        }
        catch (final XmlPullParserException|IOException exception) {
            logger.warning("Can't get info about channel");
        }
        return (chan);
    }

    private boolean isParsingOfChannelOver() throws XmlPullParserException {
        return ((parser.getEventType()==XmlPullParser.START_TAG)&&(parser.getName().equals(tags.item)))
                ||((parser.getEventType()==XmlPullParser.END_TAG)&&(parser.getName().equals(tags.channel)));
    }
    private String parseTag(@NonNull final String nameTag){
        String textOfTag = "";
        try {
            while (!((parser.getEventType() == XmlPullParser.END_TAG) && (parser.getName().equals(nameTag)))) {
                if (parser.getText() != null) {
                    textOfTag += parser.getText();
                }
                parser.nextToken();
            }
        }
        catch (final XmlPullParserException|IOException exception){
            logger.warning("Can't parse tag");
        }
        return textOfTag;
    }
}
