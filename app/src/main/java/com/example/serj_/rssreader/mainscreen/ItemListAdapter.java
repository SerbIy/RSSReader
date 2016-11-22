package com.example.serj_.rssreader.mainscreen;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.serj_.rssreader.R;
import com.example.serj_.rssreader.models.Item;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.logging.Logger;

class ItemListAdapter extends ArrayAdapter<Item> implements View.OnClickListener {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private LayoutInflater inflater;

    private static class ViewHolder{
        TextView itemTitle;
        TextView itemDescription;
        TextView itemDate;
        ViewHolder(@NonNull final View view) {
            itemTitle = (TextView) view.findViewById(R.id.title_of_item);
            itemDescription = (TextView) view.findViewById(R.id.description_of_item);
            itemDate = (TextView) view.findViewById(R.id.date_of_post);
        }
    }

    ItemListAdapter(@NonNull final Context context,@NonNull final ArrayList<Item> channels){
        super(context,R.layout.item_in_list,channels);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(@NonNull final int position, View convertView, @NonNull final ViewGroup parent) {
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
        viewHolder.itemDate.setText(item.getDateOfPost());
        logger.info("Item date "+item.getDescriptionOfPost());
        if (item.isFresh()) {
            viewHolder.itemTitle.setTypeface(null, Typeface.BOLD);
        }
        else{
            viewHolder.itemTitle.setTypeface(null, Typeface.NORMAL);
        }

        return convertView;

    }

    @Override
    public void onClick(final View v) {

    }
}
