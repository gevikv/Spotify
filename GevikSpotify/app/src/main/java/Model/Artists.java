package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 6/24/2015.
 */
public class Artists {
    public List<Artist> artistss;

    public Artists() {
        artistss = new ArrayList<>();

    }

    public boolean equals(Object obj) {
        Artists art = (Artists) obj;
        if (art.artistss.get(0).getName().equals(artistss.get(0).getName()))
            return true;
        else
            return false;


    }
}
