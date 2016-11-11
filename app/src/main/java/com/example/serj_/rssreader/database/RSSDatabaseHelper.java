package com.example.serj_.rssreader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.serj_.rssreader.models.Channel;
import com.example.serj_.rssreader.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RSSDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RSSDB";
    private static final String TABLE_CHANNELS = "channels";
    private static final String TABLE_ITEMS = "items";


    private static final String CHANNEL_LINK = "link";
    private static final String CHANNEL_LAST_UPDATE = "last_update";
    private static final String CHANNEL_NAME = "name";
    private static final String CHANNEL_DESCRIPTION = "description";

    private static final String CHANNEL_ID = "channel_id";

    private static final String ITEM_ID = "item_id";
    private static final String ITEM_LINK = "link";
    private static final String ITEM_DATE = "date";
    private static final String ITEM_TITLE = "title";
    private static final String ITEM_DESCRIPTION = "description";

    private static final Logger logger = Logger.getLogger("MyLogger");
    public RSSDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {

        final String CREATE_CHANNEL_TABLE = "CREATE TABLE "+TABLE_CHANNELS+"("+CHANNEL_ID+" INTEGER PRIMARY KEY,"
                +CHANNEL_LINK+" TEXT"+ CHANNEL_LAST_UPDATE +" TEXT"+CHANNEL_NAME+" TEXT"+CHANNEL_DESCRIPTION+" TEXT"+")";

        final String CREATE_ITEM_TABLE = "CREATE TABLE "+TABLE_ITEMS+"("+ITEM_ID+" INTEGER PRIMARY KEY," + CHANNEL_ID
                +" INTEGER FOREIGN KEY REFERENCE" + TABLE_CHANNELS+"("+CHANNEL_ID+"),"
                +ITEM_LINK+" TEXT"+ITEM_DATE+" TEXT"+ITEM_TITLE+" TEXT"+ITEM_DESCRIPTION+" TEXT"+")";

        db.execSQL(CREATE_CHANNEL_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNELS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ITEMS);
        onCreate(db);
    }

    public void addChannel(Channel chan){

        SQLiteDatabase database = this.getWritableDatabase();
        if(!isThereChannel(chan,database)){
            database.insert(TABLE_CHANNELS,null,getChannelValue(chan));
        }
        database.close();
    }
    public void addItems(ArrayList<Item> items){
        SQLiteDatabase database = this.getWritableDatabase();
        for (Item item:items){
            if(isThereItem(item,database)){
                break;
            }
            database.insert(TABLE_ITEMS,null,getItemValue(item));

        }
        database.close();
    }
    public ArrayList<Channel> getAllChannels(){
        SQLiteDatabase database = this.getWritableDatabase();
        final String Query = "SELECT  * FROM " + TABLE_CHANNELS;
        final Cursor cursor = database.rawQuery(Query, null);
        ArrayList<Channel> channels = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                Channel channel = new Channel();
                channel.setChannelID(cursor.getInt(cursor.getColumnIndex(CHANNEL_ID)));
                channel.setDescription(cursor.getString(cursor.getColumnIndex(CHANNEL_DESCRIPTION)));
                channel.setChannelLink(cursor.getString(cursor.getColumnIndex(CHANNEL_LINK)));
                channel.setChannelName(cursor.getString(cursor.getColumnIndex(CHANNEL_NAME)));
                channel.setLastUpdate(cursor.getString(cursor.getColumnIndex(CHANNEL_LAST_UPDATE)));
                channels.add(channel);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return channels;
    }
    public ArrayList<Item> getItems(List<Integer> channelsID){
        SQLiteDatabase database = this.getWritableDatabase();
        String ids = "(";
        for (Integer id:channelsID){
            ids +=id.toString()+", ";
        }
        ids = ids.substring(0,ids.length()-2);
        ids += ")";
        final String Query = "SELECT  * FROM " + TABLE_ITEMS +"WHERE" + CHANNEL_ID + " IN " + ids;
        final Cursor cursor = database.rawQuery(Query, null);
        ArrayList<Item> items = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setChannelID(cursor.getInt(cursor.getColumnIndex(CHANNEL_ID)));
                item.setDescriptionOfPost(cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION)));
                item.setLinkOfPost(cursor.getString(cursor.getColumnIndex(ITEM_LINK)));
                item.setIdOfPost(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                item.setTitleOfPost(cursor.getString(cursor.getColumnIndex(ITEM_TITLE)));
                item.setDateOfPost(cursor.getString(cursor.getColumnIndex(ITEM_DATE)));
                items.add(item);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }
    private boolean isThereChannel(Channel chan, SQLiteDatabase database){
        boolean result = false;
        try {
            final String Query = "Select * from " + TABLE_CHANNELS + " where " + CHANNEL_ID + " = " + chan.getChannelID();
            final Cursor cursor = database.rawQuery(Query, null);
            if (cursor == null) {
                result = false;
            } else {
                result = true;
                cursor.close();
            }

        }
        catch(Throwable e){
            logger.warning("Cannot connect to database");
        }
        return  result;
    }
    private boolean isThereItem (Item item, SQLiteDatabase database){
        boolean result=false;
        try {
            final String Query = "Select * from " + TABLE_ITEMS + " where " + ITEM_ID + " = " + item.getIdOfPost();
            final Cursor cursor = database.rawQuery(Query, null);
            if (cursor == null) {
                result = false;
            } else {
                result = true;
                cursor.close();
            }

        }
        catch (Throwable e){
            logger.warning("Cannot connect to database");
        }
        return  result;
    }
    private ContentValues getChannelValue(Channel chan){
        final ContentValues value = new ContentValues();
        value.put(CHANNEL_ID,chan.getChannelID());
        value.put(CHANNEL_LAST_UPDATE,chan.getLastUpdate());
        value.put(CHANNEL_DESCRIPTION,chan.getDescription());
        value.put(CHANNEL_LINK,chan.getChannelLink());
        value.put(CHANNEL_NAME,chan.getChannelName());
        return value;
    }
    private ContentValues getItemValue(Item item){
        final ContentValues value = new ContentValues();
        value.put(ITEM_ID,item.getIdOfPost());
        value.put(CHANNEL_ID,item.getChannelID());
        value.put(ITEM_DATE,item.getDateOfPost());
        value.put(ITEM_DESCRIPTION,item.getDescriptionOfPost());
        value.put(ITEM_LINK,item.getLinkOfPost());
        value.put(ITEM_TITLE,item.getTitleOfPost());
        return value;
    }
}