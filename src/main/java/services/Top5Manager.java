package services;

import models.Catalog;
import models.Song;

import java.util.List;

public class Top5Manager {

    public static List<Song> getTop5MostPurchased() {
        return Catalog.getInstance().getTop5MostPurchased();
    }

    public static List<Song> getTop5MostPlaylisted() {
        // Catalog exposes "getTop5MostAddedToPlaylist"
        return Catalog.getInstance().getTop5MostAddedToPlaylist();
    }

    public static List<Song> getTop5HighestRated() {
        // Catalog exposes "getTop5MostRated"
        return Catalog.getInstance().getTop5MostRated();
    }
}
