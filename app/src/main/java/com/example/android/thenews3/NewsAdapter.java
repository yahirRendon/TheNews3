package com.example.android.thenews3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * NewsAdapter method
 */
public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    /**
     * Returns a list item view that displays news articles
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Get the {@link News} object located at this position in the list
        News currentNews = getItem(position);

        // Find the TextView in the list_item.xml layout with the title_textview.
        TextView titleView = listItemView.findViewById(R.id.title_textview);
        titleView.setText(currentNews.getTitle());

        // Find the TextView in the list_item.xml layout with the category_textview.
        TextView categoryView = (TextView) listItemView.findViewById(R.id.category_textview);
        // Display the section name of the current News in that TextView
        categoryView.setText(currentNews.getCategoryName());

        // Find the TextView in the list_item.xml layout with the author_textview.
        TextView authorView = (TextView) listItemView.findViewById(R.id.author_textview);
        if (currentNews.getAuthor() != null) {
            // Display the author of the current News in that TextView
            authorView.setText(currentNews.getAuthor());
        } else {
            // Hide author TextView
            authorView.setVisibility(View.INVISIBLE);
        }

        // Find the TextView in the list_item.xml layout with the date_textview.
        TextView dateView = listItemView.findViewById(R.id.date_textview);
        dateView.setText(currentNews.getDate());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
