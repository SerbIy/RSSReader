package com.example.serj_.rssreader.model;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.logging.Logger;

@EqualsAndHashCode
@Getter
@Setter
public final class Item implements Parcelable,Comparable<Item> {

    private String dateOfPost;
    private String descriptionOfPost;
    private String idOfPost;
    private String titleOfPost;
    private String linkOfPost;
    private int channelID;
    private boolean isFresh;

    private static final Logger logger = Logger.getLogger(Item.class.getName());

    public Item(){
        dateOfPost = "";
        descriptionOfPost = "";
        idOfPost = "";
        titleOfPost = "";
        linkOfPost = "";
        isFresh = true;
    }


    private Item(Parcel in) {
        dateOfPost = in.readString();
        descriptionOfPost = in.readString();
        idOfPost = in.readString();
        titleOfPost = in.readString();
        linkOfPost = in.readString();
        channelID = in.readInt();
        isFresh = in.readByte() != 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(@NonNull Parcel in) {

            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {

            return new Item[size];
        }
    };

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(@NonNull final Parcel dest, final int flags) {
        dest.writeString(dateOfPost);
        dest.writeString(descriptionOfPost);
        dest.writeString(idOfPost);
        dest.writeString(titleOfPost);
        dest.writeString(linkOfPost);
        dest.writeInt(channelID);
        dest.writeByte((byte)(isFresh?1:0));
    }
    public boolean convertDate(@NonNull DateFormat format){
        boolean isDateConverted = false;
        long timeMs = 0;
        final String date = this.getDateOfPost();
        try {
            timeMs = format.parse(date).getTime();
            isDateConverted = true;
        }
        catch (final ParseException exception){
            logger.warning("Can't parse date of post "+date);
        }
        finally {
            if(isDateConverted) {
                this.setDateOfPost(String.valueOf(timeMs));
            }

        }
        return (isDateConverted);
    }

    @Override
    public int compareTo(@NonNull final Item item) {
        int result = 0;
        final Long thisItem = Long.parseLong(this.dateOfPost);
        final Long otherItem = Long.parseLong(item.getDateOfPost());
        if(thisItem>otherItem){
            result = 1;
        }else if(thisItem<otherItem){
            result=-1;
        }
        return result;
    }
}

