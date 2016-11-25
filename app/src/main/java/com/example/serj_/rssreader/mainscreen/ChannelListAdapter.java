package com.example.serj_.rssreader.mainscreen;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.serj_.rssreader.R;
import com.example.serj_.rssreader.model.Channel;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Logger;

class ChannelListAdapter extends ArrayAdapter<Channel> {


    private final LayoutInflater inflater;
    private static final Logger logger = Logger.getLogger(ChannelListAdapter.class.getName());
    private final Context context;

    private static class ViewHolder{
        final TextView channelName;
        final TextView newItems;
        final TextView lastPubDate;

        ViewHolder(final View view) {
            channelName = (TextView) view.findViewById(R.id.channel_name);
            newItems = (TextView) view.findViewById(R.id.items_count);
            lastPubDate = (TextView) view.findViewById(R.id.last_update);
        }
    }

    ChannelListAdapter(final Context context,final ArrayList<Channel> channels){
        super(context,R.layout.channel_in_list,channels);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getView(final int position,View convertView,final ViewGroup parent) {
        final Channel channel = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.channel_in_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.channelName.setText(channel.getChannelName());
        logger.info("Channel name "+channel.getChannelName());
        viewHolder.newItems.setText(String.format(Locale.getDefault(),"%d",channel.getNewItems()));
        logger.info("Channel items "+channel.getNewItems());
        final Long time = System.currentTimeMillis()-Long.parseLong(channel.getLastUpdate());
        final long seconds = time/1000;
        final long minutes = seconds/60;
        final long hours = minutes/60;
        final long days = hours/24;
        final long months = days/30;
        final long years = months/12;
        String timeString = context.getString(R.string.channel_start_of_date);
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
        viewHolder.lastPubDate.setText(timeString);
        return convertView;

    }

}
