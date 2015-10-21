package com.example.gevik.gevikspotify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import Model.Track;
import Model.Tracks;

/**
 * Created by user on 6/26/2015.
 */
public class DetailsPageAdapter extends BaseAdapter {

    Context context;

    protected Tracks tracks;
    LayoutInflater inflater;


    public DetailsPageAdapter(Context context, Tracks tracks) {
        this.tracks = tracks;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        Log.i("adapterCheck",tracks.tracks.get(0).getName());

    }


    @Override
    public int getCount() {
        return tracks.tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return tracks.tracks.get(position);
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
            convertView = this.inflater.inflate(R.layout.list_item_detasils, parent, false);
            holder.txtname = (TextView) convertView.findViewById(R.id.list_item_songname);
            holder.txtAlbum = (TextView) convertView.findViewById(R.id.list_item_album);
            holder.artistImage = (ImageView) convertView.findViewById(R.id.artist_Image);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Track track = tracks.tracks.get(position);
        holder.txtname.setText(track.getName());
        holder.txtAlbum.setText(track.getAlbum());
        holder.artistImage.setImageBitmap(track.getAlbumImage());

        return convertView;

    }

    private class ViewHolder {
        TextView txtname;
        TextView txtAlbum;
        ImageView artistImage;
    }
}
