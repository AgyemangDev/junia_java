package fr.isen.java2.db.daos;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;
import fr.isen.java2.db.entities.Movie;

public class MovieDao {

    private static final String DB_URL = "jdbc:sqlite:sqlite.db";

    // ---------- PUBLIC METHODS ----------

    public List<Movie> listMovies() {
        String sql = """
            SELECT * FROM movie
            JOIN genre ON movie.genre_id = genre.idgenre
        """;
        return executeMovieQuery(sql, null);
    }

    public List<Movie> listMoviesByGenre(String genreName) {
        String sql = """
            SELECT * FROM movie
            JOIN genre ON movie.genre_id = genre.idgenre
            WHERE genre.name = ?
        """;
        return executeMovieQuery(sql, genreName);
    }

    public Movie addMovie(Movie movie) {
        String sql = """
            INSERT INTO movie(title, release_date, genre_id, duration, director, summary)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, movie.getTitle());
            stmt.setDate(2, Date.valueOf(movie.getReleaseDate()));
            stmt.setInt(3, movie.getGenre().getId());
            stmt.setInt(4, movie.getDuration());
            stmt.setString(5, movie.getDirector());
            stmt.setString(6, movie.getSummary());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    movie.setId(keys.getInt(1));
                }
            }

            return movie;

        } catch (SQLException e) {
            throw new RuntimeException("Error while adding movie", e);
        }
    }

    // ---------- PRIVATE HELPERS ----------

    private List<Movie> executeMovieQuery(String sql, String genreName) {
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (genreName != null) {
                stmt.setString(1, genreName);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movies.add(mapMovie(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while querying movies", e);
        }

        return movies;
    }

    private Movie mapMovie(ResultSet rs) throws SQLException {
        LocalDate releaseDate = rs.getDate("release_date") != null
                ? rs.getDate("release_date").toLocalDate()
                : null;

        Genre genre = new Genre(
            rs.getInt("idgenre"),
            rs.getString("name")
        );

        return new Movie(
            rs.getInt("idmovie"),
            rs.getString("title"),
            releaseDate,
            genre,
            rs.getInt("duration"),
            rs.getString("director"),
            rs.getString("summary")
        );
    }
}