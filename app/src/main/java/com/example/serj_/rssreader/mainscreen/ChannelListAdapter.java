package com.example.serj_.rssreader.mainscreen;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.serj_.rssreader.R;
import com.example.serj_.rssreader.models.Channel;
import lombok.NonNull;
import java.util.ArrayList;
import java.util.logging.Logger;

class ChannelListAdapter extends ArrayAdapter<Channel> implements View.OnClickListener {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private LayoutInflater inflater;

    private static class ViewHolder{
        TextView channelName;
        TextView newItems;
        TextView lastPubDate;
        ViewHolder(@NonNull final View view) {
            channelName = (TextView) view.findViewById(R.id.channel_name);
            newItems = (TextView) view.findViewById(R.id.items_count);
            lastPubDate = (TextView) view.findViewById(R.id.last_update);
        }
    }

    ChannelListAdapter(@NonNull final Context context,@NonNull final ArrayList<Channel> channels){
        super(context,R.layout.channel_in_list,channels);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(@NonNull final int position,View convertView,@NonNull final ViewGroup parent) {
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
        viewHolder.newItems.setText(String.format("%d",channel.getNewItems()));
        logger.info("Channel items "+channel.getNewItems());
        return convertView;

    }

    @Override
    public void onClick(final View v) {

    }
}
