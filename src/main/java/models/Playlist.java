package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single playlist belonging to a user.
 */
public class Playlist {

    private List<Song> songs;

    public Playlist() {
        songs = new ArrayList<>();
    }

    public boolean addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
            song.incrementPlaylistAdd(); // Update stats for Top 5
            return true;
        }
        return false;
    }

    public boolean removeSong(Song song) {
        return songs.remove(song);
    }

    public boolean containsSong(Song song) {
        return songs.contains(song);
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs); // return a copy
    }

    public void clearPlaylist() {
        songs.clear();
    }

    public boolean isEmpty() {
        return songs.isEmpty();
    }

    @Override
    public String toString() {
        if (songs.isEmpty()) return "Your playlist is empty.";
        StringBuilder sb = new StringBuilder("Your Playlist:\n");
        for (Song song : songs) {
            sb.append("- ").append(song).append("\n");
        }
        return sb.toString();
    }
}
