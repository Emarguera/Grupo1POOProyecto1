package dao;

import models.Song;
import enums.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {

    // Create Song
    public boolean createSong(Song song) {
        String sql = "{call SONG_PKG.create_song(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, song.getId());
            stmt.setString(2, song.getTitle());
            stmt.setString(3, song.getArtist());
            stmt.setDouble(4, song.getPrice());
            stmt.setString(5, song.getGenre().name());

            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating song: " + e.getMessage());
            return false;
        }
    }

    // Read Song by ID (uses OUT params)
    public Song getSongById(String id) {
        String sql = "{call SONG_PKG.read_song(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, id);
            stmt.registerOutParameter(2, Types.VARCHAR); // title
            stmt.registerOutParameter(3, Types.VARCHAR); // artist
            stmt.registerOutParameter(4, Types.DOUBLE);  // price
            stmt.registerOutParameter(5, Types.VARCHAR); // genre
            stmt.registerOutParameter(6, Types.INTEGER); // purchase_count
            stmt.registerOutParameter(7, Types.INTEGER); // playlist_count
            stmt.registerOutParameter(8, Types.INTEGER); // total_rating
            stmt.registerOutParameter(9, Types.INTEGER); // rating_count

            stmt.execute();

            String title = stmt.getString(2);
            if (title == null) return null; // no record found

            String artist = stmt.getString(3);
            double price = stmt.getDouble(4);
            Genre genre = Genre.valueOf(stmt.getString(5));
            int purchaseCount = stmt.getInt(6);
            int playlistCount = stmt.getInt(7);
            int totalRating = stmt.getInt(8);
            int ratingCount = stmt.getInt(9);

            return new Song(id, title, artist, price, genre, purchaseCount, playlistCount, totalRating, ratingCount);

        } catch (SQLException e) {
            System.err.println("Error reading song: " + e.getMessage());
            return null;
        }
    }

    // Update Song (all fields)
    public boolean updateSong(Song song) {
        String sql = "{call SONG_PKG.update_song(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, song.getId());
            stmt.setString(2, song.getTitle());
            stmt.setString(3, song.getArtist());
            stmt.setDouble(4, song.getPrice());
            stmt.setString(5, song.getGenre().name());
            stmt.setInt(6, song.getPurchaseCount());
            stmt.setInt(7, song.getPlaylistCount());
            stmt.setInt(8, song.getTotalRating());
            stmt.setInt(9, song.getRatingCount());

            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating song: " + e.getMessage());
            return false;
        }
    }

    // Delete Song by ID
    public boolean deleteSong(String id) {
        String sql = "{call SONG_PKG.delete_song(?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, id);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting song: " + e.getMessage());
            return false;
        }
    }

    // List all songs
    public List<Song> listAllSongs() {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT id, title, artist, price, genre, purchase_count, playlist_count, total_rating, rating_count FROM songs";
        try (Connection conn = ConexionOracle.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                songs.add(new Song(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getDouble("price"),
                        Genre.valueOf(rs.getString("genre")),
                        rs.getInt("purchase_count"),
                        rs.getInt("playlist_count"),
                        rs.getInt("total_rating"),
                        rs.getInt("rating_count")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error listing songs: " + e.getMessage());
        }
        return songs;
    }
}
