package com.example.gevik.gevikspotify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import Model.Artist;
import Model.Artists;

/**
 * Created by user on 6/24/2015.
 */
public class FirstPageAdapter extends BaseAdapter {
    Context context;

    protected Artists artists;
    LayoutInflater inflater;

    public FirstPageAdapter(Context context, Artists artists) {
        this.context = context;
        this.artists = artists;
        this.inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {

        return artists.artistss.size();

    }

    @Override
    public Artist getItem(int position) {
        return artists.artistss.get(position);


    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.list_item, parent, false);
            holder.txtname = (TextView) convertView.findViewById(R.id.list_item_textview);
            holder.artistImage = (ImageView) convertView.findViewById(R.id.artist_Image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Artist artist = artists.artistss.get(position);
        holder.txtname.setText(artist.getName());
        holder.artistImage.setImageBitmap(artist.getArtistImage());


        return convertView;
    }

    private class ViewHolder {
        TextView txtname;
        ImageView artistImage;
    }

}
