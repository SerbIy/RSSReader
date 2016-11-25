package com.example.serj_.rssreader.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class FeedState{
    private ArrayList<Item> itemsToShow;
    private ArrayList<Channel>  channelsFilter;

    public FeedState(){
        this.itemsToShow = new ArrayList<>();
        this.channelsFilter = new ArrayList<>();
    }
}