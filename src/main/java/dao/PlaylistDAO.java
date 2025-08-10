package dao;

import models.Playlist;
import models.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

    // Create a new playlist and return its generated ID
    public int createPlaylist(Playlist playlist) {
        String sql = "{call PLAYLIST_PKG.create_playlist(?, ?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, playlist.getOwnerId());
            stmt.setString(2, playlist.getName());
            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.execute();

            int generatedId = stmt.getInt(3);
            playlist.setId(generatedId);
            return generatedId;

        } catch (SQLException e) {
            System.err.println("Error creating playlist: " + e.getMessage());
            return -1;
        }
    }

    // Add a song to a playlist
    public boolean addSongToPlaylist(int playlistId, String songId) {
        String sql = "{call PLAYLIST_PKG.add_song_to_playlist(?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setString(2, songId);

            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error adding song to playlist: " + e.getMessage());
            return false;
        }
    }

    // Remove a song from a playlist
    public boolean removeSongFromPlaylist(int playlistId, String songId) {
        String sql = "{call PLAYLIST_PKG.remove_song_from_playlist(?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setString(2, songId);

            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error removing song from playlist: " + e.getMessage());
            return false;
        }
    }

    // Delete a playlist and all its songs
    public boolean deletePlaylist(int playlistId) {
        String sql = "{call PLAYLIST_PKG.delete_playlist(?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, playlistId);

            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error deleting playlist: " + e.getMessage());
            return false;
        }
    }

    // Get a playlist by ID with all its songs loaded (requires joining playlist_songs and songs tables)
    public Playlist getPlaylistById(int playlistId) {
        String sqlPlaylist = "SELECT id, owner_id, name FROM playlists WHERE id = ?";
        String sqlSongs = "SELECT s.id, s.title, s.artist, s.price, s.genre, s.purchase_count, s.playlist_count, s.total_rating, s.rating_count " +
                          "FROM songs s JOIN playlist_songs ps ON s.id = ps.song_id WHERE ps.playlist_id = ?";

        try (Connection conn = ConexionOracle.conectar();
             PreparedStatement psPlaylist = conn.prepareStatement(sqlPlaylist);
             PreparedStatement psSongs = conn.prepareStatement(sqlSongs)) {

            psPlaylist.setInt(1, playlistId);
            ResultSet rsPlaylist = psPlaylist.executeQuery();

            if (!rsPlaylist.next()) {
                return null; // Playlist not found
            }

            Playlist playlist = new Playlist(
                rsPlaylist.getInt("id"),
                rsPlaylist.getString("owner_id"),
                rsPlaylist.getString("name")
            );

            psSongs.setInt(1, playlistId);
            ResultSet rsSongs = psSongs.executeQuery();

            while (rsSongs.next()) {
                Song song = new Song(
                    rsSongs.getString("id"),
                    rsSongs.getString("title"),
                    rsSongs.getString("artist"),
                    rsSongs.getDouble("price"),
                    enums.Genre.valueOf(rsSongs.getString("genre"))
                );

                // Manually set the counts and ratings since Song constructor sets 0 by default
                // You might want setters for these in Song.java or adjust constructor accordingly
                // Let's assume setters exist or use reflection/private methods - here I'll assume setters:

                // If no setters exist, consider adding them for these fields.
                // For now, assume we add them:

                song.setPurchaseCount(rsSongs.getInt("purchase_count"));
                song.setPlaylistCount(rsSongs.getInt("playlist_count"));
                song.setTotalRating(rsSongs.getInt("total_rating"));
                song.setRatingCount(rsSongs.getInt("rating_count"));

                playlist.addSong(song);
            }

            return playlist;

        } catch (SQLException e) {
            System.err.println("Error retrieving playlist: " + e.getMessage());
            return null;
        }
    }
}
