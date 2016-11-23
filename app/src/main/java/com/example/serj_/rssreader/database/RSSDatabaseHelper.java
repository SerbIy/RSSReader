package com.example.serj_.rssreader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.models.Item;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RSSDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    
    private static final String DATABASE_NAME = "RSSDatabase";
    private static final String TABLE_CHANNELS = "channels";
    private static final String TABLE_ITEMS = "items";


    private static final String CHANNEL_LINK = "link";
    private static final String CHANNEL_LAST_UPDATE = "last_update";
    private static final String CHANNEL_NAME = "name";
    private static final String CHANNEL_DESCRIPTION = "description";
    private static final String CHANNEL_NEW_ITEMS = "new_items";
    private static final String CHANNEL_ID = "channel_id";
    private static final String ITEM_ID = "item_id";
    private static final String ITEM_GUID = "guid";
    private static final String ITEM_LINK = "link_to_post";
    private static final String ITEM_DATE = "date_of_post";
    private static final String ITEM_TITLE = "title_of_post";
    private static final String ITEM_DESCRIPTION = "description_of_post";
    private static final String IS_ITEM_FRESH = "is_fresh";

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public RSSDatabaseHelper(@NonNull final Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull final SQLiteDatabase db) {
        logger.info("We create new table");
        final String CREATE_CHANNEL_TABLE = "CREATE TABLE "+
                TABLE_CHANNELS+"("+
                CHANNEL_ID+" INTEGER PRIMARY KEY," +
                CHANNEL_LINK+" TEXT,"+
                CHANNEL_LAST_UPDATE +" TEXT,"+
                CHANNEL_NAME+" TEXT,"+
                CHANNEL_DESCRIPTION+" TEXT,"+
                CHANNEL_NEW_ITEMS+" INTEGER"+")";
        logger.info(CREATE_CHANNEL_TABLE);
        final String CREATE_ITEM_TABLE = "CREATE TABLE "+
                TABLE_ITEMS+"("+
                ITEM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                ITEM_GUID +" TEXT,"+
                CHANNEL_ID+" INTEGER,"+
                ITEM_LINK+" TEXT,"+
                ITEM_DATE+" TEXT,"+
                ITEM_TITLE+" TEXT,"+
                ITEM_DESCRIPTION+" TEXT,"+
                IS_ITEM_FRESH+" INTEGER"+")";
        try {
            db.execSQL(CREATE_CHANNEL_TABLE);
            db.execSQL(CREATE_ITEM_TABLE);
        }
        catch (final SQLException exception){
            logger.warning("Can't create tables");
        }
    }

    @Override
    public void onUpgrade(@NonNull final SQLiteDatabase db,@NonNull final  int oldVersion,@NonNull final  int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNELS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
        catch (final SQLException exception){
            logger.warning("Can't upgrade tables");
        }
    }

    public boolean addChannel(@NonNull final Channel chan){

        boolean channelAdded = false;
        SQLiteDatabase database = null;
        try {
            database  = this.getWritableDatabase();
            if (!isThereChannel(chan, database)) {
                channelAdded = true;
                database.insert(TABLE_CHANNELS, null, getChannelValue(chan));
            }

        }
        catch (final SQLException exception){
            logger.warning("Can't add channel "+chan.getChannelName());
        }
        finally {
            if(database!=null) {
                database.close();
            }
        }
        return channelAdded;
    }

    public int addItems(@NonNull final ArrayList<Item> items){

        int counter = 0;
        SQLiteDatabase database = null;
        try {
            database = this.getWritableDatabase();
            for (Item item : items) {
                if (isThereItem(item, database)) {
                    break;
                } else {
                    counter++;
                    database.insert(TABLE_ITEMS, null, getItemValue(item));
                }
                logger.info("That's what we put in:" + item.toString());
            }

            logger.info("That's new: " + counter);
        }
        catch (final SQLException exception){
            logger.warning("Can't add items");
        }
        finally {
            if(database!=null) {
                database.close();
            }
        }

        return counter;
    }

    public void updateNewItemsOfChannel(@NonNull final int idOfChannel,@NonNull final int counter){
        try {
            final SQLiteDatabase database = this.getWritableDatabase();
            logger.info("UPDATE " + TABLE_CHANNELS + " SET " + CHANNEL_NEW_ITEMS + " = " + CHANNEL_NEW_ITEMS + " + " + counter + " WHERE " + CHANNEL_ID + " =" + idOfChannel);
            database.execSQL("UPDATE " + TABLE_CHANNELS + " SET "
                    + CHANNEL_NEW_ITEMS + " = " + CHANNEL_NEW_ITEMS + " + " + counter + " WHERE "
                    + CHANNEL_ID + " =" + idOfChannel);
            database.close();
        }
        catch (final SQLException exception){
            logger.warning("Can't update new_items field");
        }
    }
    public void updatePubDateOfChannel(@NonNull final Channel channel){
        try {
        final SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE " + TABLE_CHANNELS + " SET " + CHANNEL_LAST_UPDATE + " = " + channel.getLastUpdate()
                + " WHERE " + CHANNEL_ID + " =" +channel.getChannelID());
            database.close();
        }
        catch (final SQLException exception){
            logger.warning("Can't update pubDate field of channel");
        }
    }
    public ArrayList<Channel> getAllChannels(){
        ArrayList<Channel> channels = new ArrayList<>();
        Cursor cursor = null;
        try {
            final SQLiteDatabase database = this.getWritableDatabase();
            cursor = database.query(TABLE_CHANNELS,null,null,null,null,null,null);
            channels = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Channel channel = new Channel();
                    channel.setChannelID(cursor.getInt(cursor.getColumnIndex(CHANNEL_ID)));
                    channel.setDescription(cursor.getString(cursor.getColumnIndex(CHANNEL_DESCRIPTION)));
                    channel.setChannelLink(cursor.getString(cursor.getColumnIndex(CHANNEL_LINK)));
                    channel.setChannelName(cursor.getString(cursor.getColumnIndex(CHANNEL_NAME)));
                    channel.setLastUpdate(cursor.getString(cursor.getColumnIndex(CHANNEL_LAST_UPDATE)));
                    channel.setNewItems(cursor.getInt(cursor.getColumnIndex(CHANNEL_NEW_ITEMS)));
                    channels.add(channel);
                }
                while (cursor.moveToNext());
            }

        }
        catch (final SQLException exception){
            logger.warning("Can't get channels");
        }
        finally{
            if(cursor!=null) {
                cursor.close();
            }
        }
        return channels;
    }
    public ArrayList<Item> getAllItems(){
        ArrayList<Item> items = new ArrayList<>();
        Cursor cursor = null;
        try {
            final SQLiteDatabase database = this.getWritableDatabase();
            cursor = database.query(TABLE_ITEMS,null,null,null,null,null,null);

            if (cursor.moveToFirst()) {
                do {
                    Item item = new Item();
                    item.setChannelID(cursor.getInt(cursor.getColumnIndex(CHANNEL_ID)));
                    item.setDescriptionOfPost(cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION)));
                    item.setLinkOfPost(cursor.getString(cursor.getColumnIndex(ITEM_LINK)));
                    item.setIdOfPost(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                    item.setTitleOfPost(cursor.getString(cursor.getColumnIndex(ITEM_TITLE)));
                    item.setDateOfPost(cursor.getString(cursor.getColumnIndex(ITEM_DATE)));
                    item.setFresh(cursor.getInt(cursor.getColumnIndex(IS_ITEM_FRESH)) == 1);
                    items.add(item);
                }
                while (cursor.moveToNext());
            }

        }
        catch (final SQLException exception){
            logger.warning("Can't get all items");
        }
        finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return items;
    }

    public ArrayList<Item> getItemsById(@NonNull final int idOfChannel){
        ArrayList<Item> items = new ArrayList<>();
        Cursor cursor = null;
        try {
            final SQLiteDatabase database = this.getWritableDatabase();

           cursor = database.query(TABLE_ITEMS,null,CHANNEL_ID + " = " + idOfChannel,null,null,null,null);

            if (cursor.moveToFirst()) {
                do {
                    Item item = new Item();
                    item.setChannelID(cursor.getInt(cursor.getColumnIndex(CHANNEL_ID)));
                    item.setDescriptionOfPost(cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION)));
                    item.setLinkOfPost(cursor.getString(cursor.getColumnIndex(ITEM_LINK)));
                    item.setIdOfPost(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                    item.setTitleOfPost(cursor.getString(cursor.getColumnIndex(ITEM_TITLE)));
                    item.setDateOfPost(cursor.getString(cursor.getColumnIndex(ITEM_DATE)));
                    item.setFresh(cursor.getInt(cursor.getColumnIndex(IS_ITEM_FRESH)) == 1);
                    items.add(item);
                }
                while (cursor.moveToNext());
            }

        }
        catch (final SQLException exception){
            logger.warning("Can't get items");
        }
        finally{

                if(cursor!=null) {
                    cursor.close();
                }
        }
        return items;
    }

    private boolean isThereChannel(@NonNull final Channel chan,@NonNull final SQLiteDatabase database){
        boolean result = false;
        Cursor cursor = null;
        try {
            final String Query = "Select * from " + TABLE_CHANNELS + " where " + CHANNEL_LINK + "='" + chan.getChannelLink()+"'";
            cursor = database.rawQuery(Query, null);
            if (cursor.getCount()==0) {
                logger.info("There is no channel like that");
                result = false;
            } else {
                logger.info("There is already channel like that");
                result = true;

            }

        }
        catch(final SQLException exception){
            logger.warning("Can't connect to database");
        }
        finally {
                if(cursor!=null) {
                    cursor.close();
                }
        }
        return  result;
    }

    private boolean isThereItem (@NonNull final  Item item,@NonNull final  SQLiteDatabase database){
        boolean result = false;
        Cursor cursor = null;
        try {
            final String Query = "Select * from " + TABLE_ITEMS + " where " + ITEM_GUID + "='" +item.getIdOfPost()+"'";
            logger.info("Try to :"+Query);
            cursor = database.rawQuery(Query, null);
            if (cursor.getCount()==0) {
                cursor.close();
                logger.info("There is no item in database like that");
                result = false;
            } else {

                logger.info("That item is already in database");
                result = true;
            }

        }
        catch (final SQLException exception){
            logger.warning("Can't connect to database or something wrong with item");
        }
        finally {
                if(cursor!=null) {
                    cursor.close();
                }
        }
        return  result;
    }
    private ContentValues getChannelValue(@NonNull final Channel chan){
        final ContentValues value = new ContentValues();
        value.put(CHANNEL_ID,chan.getChannelID());
        value.put(CHANNEL_LAST_UPDATE,chan.getLastUpdate());
        value.put(CHANNEL_DESCRIPTION,chan.getDescription());
        value.put(CHANNEL_LINK,chan.getChannelLink());
        value.put(CHANNEL_NAME,chan.getChannelName());
        value.put(CHANNEL_NEW_ITEMS,chan.getNewItems());
        logger.info(" Items in Value"+chan.getNewItems());
        return value;
    }
    private ContentValues getItemValue(@NonNull final Item item){
        final ContentValues value = new ContentValues();
        value.put(ITEM_GUID,item.getIdOfPost());
        value.put(CHANNEL_ID,item.getChannelID());
        value.put(ITEM_DATE,item.getDateOfPost());
        value.put(ITEM_DESCRIPTION,item.getDescriptionOfPost());
        value.put(ITEM_LINK,item.getLinkOfPost());
        value.put(ITEM_TITLE,item.getTitleOfPost());
        value.put(IS_ITEM_FRESH,(item.isFresh()?1:0));
        return value;
    }

}