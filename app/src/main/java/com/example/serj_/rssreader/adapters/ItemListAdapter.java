package com.example.serj_.rssreader.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.serj_.rssreader.R;
import com.example.serj_.rssreader.models.Item;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ItemListAdapter extends ArrayAdapter<Item> implements View.OnClickListener {

    private static final Logger logger = Logger.getLogger("MyLogger");
    private Context context;
    private ArrayList<Item> items;
    private LayoutInflater inflater;

    private static class ViewHolder{
        TextView itemTitle;
        TextView itemDescription;
        TextView itemDate;
        public ViewHolder(View view) {
            itemTitle = (TextView) view.findViewById(R.id.title_of_item);
            itemDescription = (TextView) view.findViewById(R.id.description_of_item);
            itemDate = (TextView) view.findViewById(R.id.date_of_post);
        }
    }

    public ItemListAdapter(Context context, ArrayList<Item> channels){
        super(context,R.layout.item_in_list,channels);
        inflater = LayoutInflater.from(context);
        logger.info(context.toString());
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Item item = getItem(position);
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
