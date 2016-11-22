package com.example.serj_.rssreader.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;



@EqualsAndHashCode
@Setter
@Getter
public final class Channel implements Parcelable {

    private String channelName;
    private String channelLink;
    private int channelID;
    private String lastUpdate;
    private String description;
    private int newItems;

    public Channel(){

        newItems = 0;
    }

    private Channel(@NonNull final Parcel in) {
        channelName = in.readString();
        channelLink = in.readString();
        channelID = in.readInt();
        lastUpdate = in.readString();
        description = in.readString();
        newItems = in.readInt();
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(@NonNull final Parcel in) {

            return new Channel(in);
        }

        @Override
        public Channel[] newArray(@NonNull final int size) {

            return new Channel[size];
        }
    };

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(@NonNull final Parcel dest,@NonNull final int flags) {
        dest.writeString(channelName);
        dest.writeString(channelLink);
        dest.writeInt(channelID);
        dest.writeString(lastUpdate);
        dest.writeString(description);
        dest.writeInt(newItems);
    }
}
