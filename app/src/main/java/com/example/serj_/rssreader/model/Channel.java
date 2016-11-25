package com.example.serj_.rssreader.model;


import android.os.Parcel;
import android.os.Parcelable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.logging.Logger;


@EqualsAndHashCode
@Setter
@Getter
public final class Channel implements Parcelable,Comparable<Channel> {

    private String channelName;
    private String channelLink;
    private String lastUpdate;
    private String description;
    private int newItems;
    private int channelID;

    private static final Logger logger = Logger.getLogger(Channel.class.getName());

    public Channel(){

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
        public Channel[] newArray(final int size) {

            return new Channel[size];
        }
    };

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(@NonNull final Parcel dest,final int flags) {
        dest.writeString(channelName);
        dest.writeString(channelLink);
        dest.writeInt(channelID);
        dest.writeString(lastUpdate);
        dest.writeString(description);
        dest.writeInt(newItems);
    }
    public boolean convertDate(@NonNull DateFormat format){
            boolean isDateConverted = false;
            long timeMs = 0;
            final String date = this.getLastUpdate();
            try {
                timeMs = format.parse(date).getTime();
                isDateConverted = true;
            }
            catch (final ParseException exception){
                logger.warning("Can't parse date of last update "+date);
                logger.warning(exception.toString());
            }
            finally {
                if(isDateConverted) {
                    this.setLastUpdate(String.valueOf(timeMs));
                }

            }
            return (isDateConverted);
    }

    @Override
    public int compareTo(@NonNull final Channel channel) {
        int result = 0;
        final Long thisChannel = Long.parseLong(this.getLastUpdate());
        final Long otherChannel = Long.parseLong(channel.getLastUpdate());
        if(thisChannel>otherChannel){
            result = 1;
        }else if(thisChannel<otherChannel){
            result=-1;
        }
        return result;
    }
}
