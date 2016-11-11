package com.example.serj_.rssreader.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public final class Item {

    private String dateOfPost;
    private String descriptionOfPost;
    private String idOfPost;
    private String titleOfPost;
    private String linkOfPost;
    private int channelID;

    public Item(){
    }

}

