package com.example.serj_.rssreader.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by serj_ on 16.10.2016.
 */

@EqualsAndHashCode
@Getter
@Setter
public final class FeedItem {

    private String dateOfPost;
    private String descriptionOfPost;
    private String guidOfPost;
    private String titleOfPost;
    private String linkOfPost;
    private int channelID;

    public FeedItem(){
    }

}

