package models;

import dao.SongDAO;
import enums.Genre;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Singleton class representing the catalog of songs.
 */
public class Catalog {

    private static Catalog instance;
    private List<Song> songs;
    private int songIdCounter = 100; // starts from 100

    private Catalog() {
        songs = new ArrayList<>();
        loadSongsFromDB();
    }

    public static Catalog getInstance() {
        if (instance == null) {
            instance = new Catalog();
        }
        return instance;
    }

    public void loadSongsFromDB() {
        SongDAO songDAO = new SongDAO();
        List<Song> loadedSongs = songDAO.listAllSongs();  // <-- use listAllSongs() to load all songs
        if (loadedSongs != null) {
            songs = new ArrayList<>(loadedSongs);

            // Update songIdCounter to avoid ID conflicts if using numeric IDs
            songIdCounter = songs.stream()
                                 .map(Song::getId)
                                 .filter(id -> id.matches("\\d+")) // only numeric IDs
                                 .mapToInt(Integer::parseInt)
                                 .max()
                                 .orElse(99) + 1;
        }
    }

    public boolean addSong(String title, String artist, double price, Genre genre) {
        String newId = String.valueOf(songIdCounter++);
        Song newSong = new Song(newId, title, artist, price, genre);
        return songs.add(newSong);
    }

    public boolean removeSong(String songId) {
        Song song = findById(songId);
        if (song != null) {
            return songs.remove(song);
        }
        return false;
    }

    public Song findById(String songId) {
        for (Song song : songs) {
            if (song.getId().equals(songId)) {
                return song;
            }
        }
        return null;
    }

    public List<Song> getAllSongs() {
        return new ArrayList<>(songs); // return a copy
    }

    public List<Song> getTop5MostPurchased() {
        return songs.stream()
                .sorted(Comparator.comparingInt(Song::getPurchaseCount).reversed())
                .limit(5)
                .toList();
    }

    public List<Song> getTop5MostRated() {
        return songs.stream()
                .filter(s -> s.getRatingCount() > 0)
                .sorted((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()))
                .limit(5)
                .toList();
    }

    public List<Song> getTop5MostAddedToPlaylist() {
        return songs.stream()
                .sorted(Comparator.comparingInt(Song::getPlaylistCount).reversed())
                .limit(5)
                .toList();
    }
}
