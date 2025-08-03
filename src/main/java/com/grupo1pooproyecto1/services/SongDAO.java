/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.services;

/**
 *
 * @author isaac
 */
import com.grupo1pooproyecto1.enums.Genre;
import com.grupo1pooproyecto1.models.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    private final Connection connection;

    public SongDAO(Connection connection) {
        this.connection = connection;
    }

    // Insertar canción
public boolean insert(Song song) throws SQLException {
    String seqSQL = "SELECT songs_seq.NEXTVAL FROM dual";
    int nextId = 0;
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(seqSQL)) {
        if (rs.next()) {
            nextId = rs.getInt(1);
        } else {
            throw new SQLException("No se pudo obtener el siguiente valor de la secuencia.");
        }
    }

    String sql = "INSERT INTO songs (song_id, title, genre, artist, album, price, purchase_count, playlist_inclusion_count) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, nextId); 
        ps.setString(2, song.getTitle());
        ps.setString(3, song.getGenre().name());
        ps.setString(4, song.getArtist());
        ps.setString(5, song.getAlbum());
        ps.setDouble(6, song.getPrice());
        ps.setInt(7, song.getPurchaseCount());
        ps.setInt(8, song.getPlaylistInclusionCount());

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            return false;
        }

        song.setSongId(nextId); 
        return true;
    }
}


    // Buscar por ID
    public Song findById(int songId) throws SQLException {
        String sql = "SELECT * FROM songs WHERE song_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, songId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Song song = new Song();
                    song.setSongId(rs.getInt("song_id"));
                    song.setTitle(rs.getString("title"));
                    song.setGenre(Genre.valueOf(rs.getString("genre")));
                    song.setArtist(rs.getString("artist"));
                    song.setAlbum(rs.getString("album"));
                    song.setPrice(rs.getDouble("price"));
                    song.setPurchaseCount(rs.getInt("purchase_count"));
                    song.setPlaylistInclusionCount(rs.getInt("playlist_inclusion_count"));
                    return song;
                }
            }
        }
        return null;
    }

    // Actualizar canción
    public boolean update(Song song) throws SQLException {
        String sql = "UPDATE songs SET title=?, genre=?, artist=?, album=?, price=?, purchase_count=?, playlist_inclusion_count=? WHERE song_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, song.getTitle());
            ps.setString(2, song.getGenre().name());
            ps.setString(3, song.getArtist());
            ps.setString(4, song.getAlbum());
            ps.setDouble(5, song.getPrice());
            ps.setInt(6, song.getPurchaseCount());
            ps.setInt(7, song.getPlaylistInclusionCount());
            ps.setInt(8, song.getSongId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Eliminar canción
    public boolean delete(int songId) throws SQLException {
        String sql = "DELETE FROM songs WHERE song_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, songId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Listar todas las canciones 
    public List<Song> findAll() throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Song song = new Song(
                        rs.getInt("song_id"),
                        rs.getString("title"),
                        Genre.valueOf(rs.getString("genre")),
                        rs.getString("artist"),
                        rs.getString("album"),
                        rs.getDouble("price"),
                        rs.getInt("purchase_count"),
                        rs.getInt("playlist_inclusion_count")
                );
                songs.add(song);

            }
        }
        

        return songs;
    }
}
