package com.example.serj_.rssreader.models;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public final class Item implements Parcelable {

    private String dateOfPost;
    private String descriptionOfPost;
    private String idOfPost;
    private String titleOfPost;
    private String linkOfPost;
    private int channelID;
    private boolean isFresh;

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
        public Item createFromParcel(Parcel in) {

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
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(dateOfPost);
        dest.writeString(descriptionOfPost);
        dest.writeString(idOfPost);
        dest.writeString(titleOfPost);
        dest.writeString(linkOfPost);
        dest.writeInt(channelID);
        dest.writeByte((byte)(isFresh?1:0));
    }
}

