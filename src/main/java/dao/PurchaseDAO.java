package dao;

import models.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

public class PurchaseDAO {

    private SongDAO songDAO = new SongDAO();

    // Calls purchase_pkg.purchase_song to add a purchase and increment count
    public boolean purchaseSong(String userId, String songId) {
        String sql = "{call purchase_pkg.purchase_song(?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, songId);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error purchasing song: " + e.getMessage());
            return false;
        }
    }

    // Calls purchase_pkg.remove_purchase to remove a purchase and decrement count
    public boolean removePurchase(String userId, String songId) {
        String sql = "{call purchase_pkg.remove_purchase(?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, songId);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error removing purchase: " + e.getMessage());
            return false;
        }
    }

    // Calls purchase_pkg.list_purchased_songs which returns a SYS_REFCURSOR, map results to Song objects
    public List<Song> listPurchasedSongs(String userId) {
        List<Song> purchasedSongs = new ArrayList<>();
        String sql = "{call purchase_pkg.list_purchased_songs(?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, userId);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);  // OracleTypes requires oracle.jdbc.OracleTypes

            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                while (rs.next()) {
                    // Build Song using the full constructor with stats
                    purchasedSongs.add(new Song(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getDouble("price"),
                        enums.Genre.valueOf(rs.getString("genre")),
                        rs.getInt("purchase_count"),
                        rs.getInt("playlist_count"),
                        rs.getInt("total_rating"),
                        rs.getInt("rating_count")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error listing purchased songs: " + e.getMessage());
        }
        return purchasedSongs;
    }
}
