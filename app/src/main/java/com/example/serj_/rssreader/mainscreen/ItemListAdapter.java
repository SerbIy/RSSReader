package com.example.serj_.rssreader.mainscreen;


import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.serj_.rssreader.R;
import com.example.serj_.rssreader.model.Item;

import java.util.ArrayList;
import java.util.logging.Logger;

class ItemListAdapter extends ArrayAdapter<Item>{

    private final LayoutInflater inflater;
    private static final Logger logger = Logger.getLogger(ItemListAdapter.class.getName());
    private final Context context;

    private static class ViewHolder{
        final TextView itemTitle;
        final TextView itemDescription;
        final TextView itemDate;
        ViewHolder(final View view) {
            itemTitle = (TextView) view.findViewById(R.id.title_of_item);
            itemDescription = (TextView) view.findViewById(R.id.description_of_item);
            itemDate = (TextView) view.findViewById(R.id.date_of_post);
        }
    }

    ItemListAdapter(final Context context,final ArrayList<Item> items){
        super(context,R.layout.item_in_list,items);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Item item = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_in_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemTitle.setText(item.getTitleOfPost());
        logger.info("Item title "+item.getTitleOfPost());
        viewHolder.itemDescription.setText(item.getDescriptionOfPost());
        logger.info("Item description "+item.getDescriptionOfPost());
        final Long time = System.currentTimeMillis()-Long.parseLong(item.getDateOfPost());
        final long seconds = time/1000;
        final long minutes = seconds/60;
        final long hours = minutes/60;
        final long days = hours/24;
        final long months = days/30;
        final long years = months/12;
        String timeString = context.getString(R.string.item_start_of_date);
        boolean endOfString = true;
        if(years>=1){
            timeString += context.getResources().getQuantityString(R.plurals.year,(int)years,(int)years);
        }else if(months>=1){
            timeString += context.getResources().getQuantityString(R.plurals.month,(int)months,(int)months);
        }else if(days>=1){
            timeString += context.getResources().getQuantityString(R.plurals.day,(int)days,(int)days);
        }else if(hours>=1){
            timeString += context.getResources().getQuantityString(R.plurals.hour,(int)hours,(int)hours);
        }else if(minutes>=1){
            timeString += context.getResources().getQuantityString(R.plurals.min,(int)minutes,(int)minutes);
        }else {
            timeString += context.getString(R.string.recently_added);
            endOfString = false;
        }
        if(endOfString){
            timeString+=context.getString(R.string.end_of_date);
        }
        viewHolder.itemDate.setText(timeString);
        logger.info("Item date "+years+":"+months+":"+days+":"+hours+":"+minutes+":"+seconds);
        if (item.isFresh()) {
            viewHolder.itemTitle.setTypeface(null, Typeface.BOLD);
        }
        else{
            viewHolder.itemTitle.setTypeface(null, Typeface.NORMAL);
        }
        convertView.setTag(R.integer.LINK_TAG,item.getLinkOfPost());
        convertView.setTag(R.integer.ID_TAG,item.getIdOfPost());
        return convertView;

    }
}
