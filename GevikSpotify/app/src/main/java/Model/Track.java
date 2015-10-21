package Model;

import android.graphics.Bitmap;

/**
 * Created by user on 6/24/2015.
 */


public class Track {

    private String id;
    private String name;
    private String url;
    private String album;
    private Bitmap albumImage;

    public String getPriview_url() {
        return priview_url;
    }

    public void setPriview_url(String priview_url) {
        this.priview_url = priview_url;
    }

    private String priview_url;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Bitmap getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(Bitmap albumImage) {
        this.albumImage = albumImage;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
