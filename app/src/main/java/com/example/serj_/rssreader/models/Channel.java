package com.example.serj_.rssreader.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;



/**
 * Created by serj_ on 20.10.2016.
 */
@EqualsAndHashCode
@Setter
@Getter
public final class Channel {

    private String channelName;
    private String channelLink;
    private int channelID;
    private String lastUpdate;
    private String description;

    public Channel(){

    }
}
