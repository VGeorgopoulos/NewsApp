package com.example.android.newsapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by neuromancer on 14/12/2016.
 */


/**
 * An {@link NewsAdapter} knows how to create a list item layout for each news article
 * in the data source (a list of {@link News} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    private static final String DATE_SEPARATOR_T = "T";
    private static final String DATE_SEPARATOR_Z = "Z";

    /**
     * Constructs a new (@link {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news    is the list of news, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if there is an existing listitem view (called convertView) that we can reuse,
        //otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        //Find the news at the given position in the list of news
        News currentNews = getItem(position);

        //Find the TextView with view ID thumbnail
        ImageView thumbnailView = (ImageView) listItemView.findViewById(R.id.thumbnail);
        if (!currentNews.getThumbnail().isEmpty() && currentNews.getThumbnail() != null) {
            thumbnailView.setVisibility(View.VISIBLE);
            thumbnailView.setImageURI(Uri.parse(currentNews.getThumbnail()));
        } else {
            thumbnailView.setVisibility(View.GONE);
        }


        //Find the TextView with view ID section_name
        //and display section of the current news in the TextView
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section_name);
        sectionView.setText(currentNews.getSectionName());

        //Find the TextView with view ID news_title
        //and display the title of the current news in the TextView
        TextView newsTitleView = (TextView) listItemView.findViewById(R.id.news_title);
        newsTitleView.setText(currentNews.getWebTitle());


        //Published date is in the form of 2016-11-23T12:18:04Z
        //So we separate it to display Date and Time in two different TextViews
        String originalDateTime = currentNews.getPublicationDate();

        String date = null;
        String time = null;
        if (originalDateTime.contains(DATE_SEPARATOR_T)) {
            String[] parts = originalDateTime.split(DATE_SEPARATOR_T);

            date = parts[0];
            if (parts[1].contains(DATE_SEPARATOR_Z)) {
                parts = parts[1].split(DATE_SEPARATOR_Z);
                time = parts[0];
            }
        }

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(date);

        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        timeView.setText(time);


        return listItemView;
    }
}
