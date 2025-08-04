package com.grupo1pooproyecto1.views;

import com.grupo1pooproyecto1.enums.Genre;
import com.grupo1pooproyecto1.models.Song;
import com.grupo1pooproyecto1.dao.SongDAO;
import com.grupo1pooproyecto1.dao.ConexionOracle; 
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class songDAOtest {
//listar caciones 
    public void showSongCatalog() {
        try {
            Connection conn = ConexionOracle.conectar();
            SongDAO dao = new SongDAO(conn);
            List<Song> canciones = dao.findAll();

            if (canciones.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay canciones registradas.");
                return;
            }

            // Encabezados de la tabla
            String[] columnas = {"ID", "Título", "Género", "Artista", "Álbum", "Precio", "Compras", "En Playlist"};

            // Modelo de la tabla
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

            for (Song s : canciones) {
                Object[] fila = {
                    s.getSongId(),
                    s.getTitle(),
                    s.getGenre(),
                    s.getArtist(),
                    s.getAlbum(),
                    s.getPrice(),
                    s.getPurchaseCount(),
                    s.getPlaylistInclusionCount()
                };
                modelo.addRow(fila);
            }

            JTable tabla = new JTable(modelo);
            JScrollPane scrollPane = new JScrollPane(tabla);
            tabla.setFillsViewportHeight(true);

            JOptionPane.showMessageDialog(
                    null,
                    scrollPane,
                    "🎵 Catálogo de Canciones",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al acceder a la base de datos: " + e.getMessage());
        }
    }
//Añadir cancion
    public void addSongTest() {
        try {
            String title = JOptionPane.showInputDialog("Ingrese el título:");
            if (title == null || title.isBlank()) {
                return;
            }

            String artist = JOptionPane.showInputDialog("Ingrese el artista:");
            if (artist == null || artist.isBlank()) {
                return;
            }

            String album = JOptionPane.showInputDialog("Ingrese el álbum:");
            if (album == null || album.isBlank()) {
                return;
            }

            String priceStr = JOptionPane.showInputDialog("Ingrese el precio:");
            if (priceStr == null || priceStr.isBlank()) {
                return;
            }

            double price = Double.parseDouble(priceStr);

            // Desplegable para género
            String[] genreOptions = Arrays.stream(Genre.values())
                    .map(Enum::name)
                    .toArray(String[]::new);

            String genreStr = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione el género:",
                    "Género",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    genreOptions,
                    genreOptions[0]
            );

            if (genreStr == null || genreStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No seleccionó ningún género.");
                return;
            }

            System.out.println("Género seleccionado: '" + genreStr + "'");

            Genre genre;

            try {
                genre = Genre.valueOf(genreStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Género inválido: '" + genreStr + "'");
                return;
            }
            System.out.println("Título: " + title);
            System.out.println("Género: " + genre);
            System.out.println("Artista: " + artist);
            System.out.println("Álbum: " + album);
            System.out.println("Precio: " + price);
            

            // Crear canción y guardar en BD
            Song nueva = new Song(0, title, genre, artist, album, price, 0, 0);
            Connection conn = ConexionOracle.conectar();
            SongDAO dao = new SongDAO(conn);
            dao.insert(nueva);

            JOptionPane.showMessageDialog(null, "Canción agregada correctamente.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Precio inválido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

    }
//aliminar caciones 
    public void deleteSong() {
        String input = JOptionPane.showInputDialog(null, "Ingrese el ID de la canción a eliminar:");
        if (input != null) {
            try {
                int songId = Integer.parseInt(input);
                SongDAO dao = new SongDAO(ConexionOracle.conectar());
                boolean deleted = dao.delete(songId);
                if (deleted) {
                    JOptionPane.showMessageDialog(null, "Canción eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró canción con ese ID.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID inválido.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error en la base de datos: " + e.getMessage());
            }
        }
    }
    
    public void editSong(){try {
        String idInput = JOptionPane.showInputDialog(null, "Ingrese el ID de la canción a editar:");
        if (idInput == null) return; 
        int songId = Integer.parseInt(idInput);

        SongDAO dao = new SongDAO(ConexionOracle.conectar());
        Song song = dao.findById(songId);  
        if (song == null) {
            JOptionPane.showMessageDialog(null, "Canción no encontrada.");
            return;
        }

        String title = JOptionPane.showInputDialog(null, "Título:", song.getTitle());
        if (title == null) return;

        String[] genreOptions = {
            "ROCK", "POP", "JAZZ", "CLASSICAL",
            "HIPHOP", "ELECTRONICA", "COUNTRY", "REGGAE",
            "METAL", "OTHER"
        };
        String genreStr = (String) JOptionPane.showInputDialog(
            null,
            "Seleccione el género:",
            "Género",
            JOptionPane.PLAIN_MESSAGE,
            null,
            genreOptions,
            song.getGenre().name()
        );
        if (genreStr == null) return;
        Genre genre = Genre.valueOf(genreStr);

        String artist = JOptionPane.showInputDialog(null, "Artista:", song.getArtist());
        if (artist == null) return;

        String album = JOptionPane.showInputDialog(null, "Álbum:", song.getAlbum());
        if (album == null) return;

        String priceInput = JOptionPane.showInputDialog(null, "Precio:", String.valueOf(song.getPrice()));
        if (priceInput == null) return;
        double price = Double.parseDouble(priceInput);

        Song updatedSong = new Song(songId, title, genre, artist, album, price, song.getPurchaseCount(), song.getPlaylistInclusionCount());

        boolean actualizado = dao.update(updatedSong);
        if (actualizado) {
            JOptionPane.showMessageDialog(null, "Canción actualizada correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar la canción.");
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Número inválido ingresado.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }};


}
