package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single playlist belonging to a user.
 */
public class Playlist {

    private int id;               // playlist ID from DB
    private String ownerId;       // ID of the RegisteredUser who owns this playlist
    private String name;          // playlist name
    private List<Song> songs;     // songs in the playlist

    // Default constructor
    public Playlist() {
        songs = new ArrayList<>();
    }

    // Constructor with all fields except songs list (starts empty)
    public Playlist(int id, String ownerId, String name) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.songs = new ArrayList<>();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs); // Return copy to avoid external modifications
    }

    // Add a song to the playlist, increment playlist count on the song if added
    public boolean addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
            song.incrementPlaylistAdd();
            return true;
        }
        return false;
    }

    // Remove a song from the playlist
    public boolean removeSong(Song song) {
        return songs.remove(song);
    }

    public boolean containsSong(Song song) {
        return songs.contains(song);
    }

    // Clear all songs from the playlist
    public void clearPlaylist() {
        songs.clear();
    }

    public boolean isEmpty() {
        return songs.isEmpty();
    }

    @Override
    public String toString() {
        if (songs.isEmpty()) return "Your playlist is empty.";
        StringBuilder sb = new StringBuilder("Playlist: " + name + "\n");
        for (Song song : songs) {
            sb.append("- ").append(song).append("\n");
        }
        return sb.toString();
    }
}
