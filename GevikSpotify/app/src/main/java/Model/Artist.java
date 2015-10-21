package Model;

import android.graphics.Bitmap;

/**
 * Created by user on 6/24/2015.
 */
public class Artist {

    public Artist() {


    }
    private String name;
    private String id;
    private String url;
    private Bitmap artistImage;




    public Bitmap getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(Bitmap artistImage) {
        this.artistImage = artistImage;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
