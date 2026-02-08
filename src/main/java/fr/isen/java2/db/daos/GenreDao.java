package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {

    private final DataSource dataSource = DataSourceFactory.getDataSource();

    public List<Genre> listGenres() {
        List<Genre> genres = new ArrayList<>();
        String sqlQuery = "SELECT * FROM genre";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                genres.add(new Genre(
                    resultSet.getInt("idgenre"),
                    resultSet.getString("name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while listing genres", e);
        }

        return genres;
    }

    public Optional<Genre> getGenre(String name) {
        String sqlQuery = "SELECT * FROM genre WHERE name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Genre(
                        resultSet.getInt("idgenre"),
                        resultSet.getString("name")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting genre by name", e);
        }

        return Optional.empty();
    }

    public void addGenre(String name) {
        String sqlQuery = "INSERT INTO genre(name) VALUES (?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, name);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error while adding genre", e);
        }
    }
}