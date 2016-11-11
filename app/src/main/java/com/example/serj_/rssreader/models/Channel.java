package com.example.serj_.rssreader.models;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;



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
