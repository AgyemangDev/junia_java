package fr.isen.java2.db.daos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.isen.java2.db.entities.Genre;
import fr.isen.java2.db.entities.Movie;

public class MovieDaoTestCase {

    @BeforeEach
    public void initDb() throws Exception {
        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS genre (
                    idgenre INTEGER PRIMARY KEY AUTOINCREMENT,
                    name VARCHAR(50) NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS movie (
                    idmovie INTEGER PRIMARY KEY AUTOINCREMENT,
                    title VARCHAR(100) NOT NULL,
                    release_date DATETIME,
                    genre_id INT NOT NULL,
                    duration INT,
                    director VARCHAR(100) NOT NULL,
                    summary TEXT,
                    CONSTRAINT genre_fk FOREIGN KEY (genre_id) REFERENCES genre (idgenre)
                )
            """);

            stmt.executeUpdate("DELETE FROM movie");
            stmt.executeUpdate("DELETE FROM genre");
            stmt.executeUpdate("DELETE FROM sqlite_sequence");

            stmt.executeUpdate("INSERT INTO genre(idgenre, name) VALUES (1,'Drama'), (2,'Comedy')");

            stmt.executeUpdate("""
                INSERT INTO movie(title, release_date, genre_id, duration, director, summary) VALUES
                ('Title 1','2015-11-26 12:00:00',1,120,'director 1','summary 1'),
                ('My Title 2','2015-11-14 12:00:00',2,114,'director 2','summary 2'),
                ('Third title','2015-12-12 12:00:00',2,176,'director 3','summary 3')
            """);
        }
    }

    @Test
    public void shouldListMovies() {
        MovieDao movieDao = new MovieDao();

        List<Movie> movies = movieDao.listMovies();

        assertThat(movies).hasSize(3);
        assertThat(movies)
            .extracting("id", "title", "duration")
            .containsExactly(
                tuple(1, "Title 1", 120),
                tuple(2, "My Title 2", 114),
                tuple(3, "Third title", 176)
            );

        assertThat(movies.get(0).getGenre().getName()).isEqualTo("Drama");
    }

    @Test
    public void shouldListMoviesByGenre() {
        MovieDao movieDao = new MovieDao();

        List<Movie> comedy = movieDao.listMoviesByGenre("Comedy");

        assertThat(comedy).hasSize(2);
        assertThat(comedy)
            .extracting("title")
            .containsExactly("My Title 2", "Third title");
    }

    @Test
    public void shouldAddMovie() {
        MovieDao movieDao = new MovieDao();

        Movie movie = new Movie(
            "New Movie",
            LocalDate.of(2024, 1, 15),
            new Genre(1, "Drama"),
            135,
            "New Director",
            "New Summary"
        );

        Movie saved = movieDao.addMovie(movie);

        assertThat(saved.getId()).isNotNull();
        assertThat(movieDao.listMovies()).hasSize(4);
    }
}