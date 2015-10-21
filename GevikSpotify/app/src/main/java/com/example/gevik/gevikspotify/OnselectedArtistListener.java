package com.example.gevik.gevikspotify;

import Model.Artist;

/**
 * Created by user on 6/26/2015.
 */
public interface OnselectedArtistListener {
    void selectedArtistListener(int position, Artist art);
    void selectedSongListener(int position,Model.Tracks tr,String artistName );

}
