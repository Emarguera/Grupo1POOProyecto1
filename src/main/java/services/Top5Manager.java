package services;

import java.util.List;
import models.Catalog;
import models.Song;

/**
 * Helper class to retrieve Top 5 statistics from the Catalog.
 */
public class Top5Manager {

    /**
     * Get Top 5 most purchased songs.
     * @return list of top 5 songs
     */
    public static List<Song> getTop5MostPurchased() {
        return Catalog.getInstance().getTop5MostPurchased();
    }

    /**
     * Get Top 5 most added to playlists.
     * @return list of top 5 songs
     */
    public static List<Song> getTop5MostPlaylisted() {
        return Catalog.getInstance().getTop5MostAddedToPlaylist();
    }

    /**
     * Get Top 5 highest rated songs.
     * @return list of top 5 songs
     */
    public static List<Song> getTop5HighestRated() {
        return Catalog.getInstance().getTop5MostRated();
    }
}
