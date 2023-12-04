package com.albumstore.util;

import com.albumstore.bean.Album;
import com.albumstore.bean.Review;

import java.sql.*;
import java.util.Optional;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql:aws://cs6650sw.c8gbw2t0j8gf.us-west-2.rds.amazonaws.com:3306/albumDB";
    private static final String USER = "cs6650sw";
    private static final String PASSWORD = "cs6650sw";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("software.aws.rds.jdbc.mysql.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void saveAlbum(Album album) throws SQLException, ClassNotFoundException {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO albums (title, artist, year, image) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, album.getTitle());
                statement.setString(2, album.getArtist());
                statement.setString(3, album.getYear());
                statement.setBytes(4, album.getImage());
                statement.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw e;
        }
    }

    public static void saveReview(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (album_id, is_like) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, review.getAlbumID());
            statement.setBoolean(2, review.isLike());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<Album> getAlbumByKey(int id) throws SQLException {
        String sql = "SELECT * FROM albums WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Album album = new Album();
                album.setId(rs.getInt("id"));
                album.setTitle(rs.getString("title"));
                album.setArtist(rs.getString("artist"));
                album.setYear(rs.getString("year")); // Assuming year is stored as a VARCHAR or similar
                byte[] imageData = rs.getBytes("image");
                if (imageData != null) {
                    album.setImage(imageData);
                }
                return Optional.of(album);
            } else {
                return Optional.empty();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
