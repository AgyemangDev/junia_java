# Junia Java Movie Collection


A simple Java application to manage a movie collection with genres using **SQLite** as the database and **Maven** for build management. The project includes DAO (Data Access Object) classes for interacting with the database and JUnit test cases to validate functionality.


---


## Features


- List all movies and genres
- Add new movies and genres
- Query movies by genre
- Uses SQLite for a lightweight database
- Unit tests with JUnit 5 and AssertJ


---


## Project Structure



junia_java/
│
├─ src/
│ ├─ main/java/fr/isen/java2/db/daos/ # DAO classes
│ │ ├─ MovieDao.java
│ │ ├─ GenreDao.java
│ │ └─ DataSourceFactory.java
│ │
│ └─ main/java/fr/isen/java2/db/entities/ # Entity classes
│ ├─ Movie.java
│ └─ Genre.java
│
├─ src/test/java/fr/isen/java2/db/daos/ # Unit tests
│ ├─ MovieDaoTestCase.java
│ └─ GenreDaoTestCase.java
│
├─ pom.xml # Maven configuration
└─ .gitignore



---


## Prerequisites


- Java 21 or higher
- Maven
- SQLite (database file `sqlite.db` will be created automatically)


---


## Setup


1. Clone or download the project:


```bash
git clone <your-repo-url>
cd junia_java

Build the project with Maven:

mvn clean install

Run the tests to verify everything is working:

mvn test
Usage

The database will be automatically created at sqlite.db.

DAO classes handle interactions with the database:

// Example: Listing all genres
GenreDao genreDao = new GenreDao();
List<Genre> genres = genreDao.listGenres();
genres.forEach(System.out::println);

Add a new movie:

MovieDao movieDao = new MovieDao();
Movie movie = new Movie(
    "New Movie Title",
    LocalDate.of(2024, 1, 15),
    new Genre(1, "Drama"),
    135,
    "New Director",
    "This is a summary"
);
movieDao.addMovie(movie);
Testing

Unit tests are written using JUnit 5 and AssertJ.

Run all tests with:

mvn test

Tests include:

GenreDaoTestCase – tests genre listing, retrieval, and insertion

MovieDaoTestCase – tests movie listing, insertion, and filtering by genre

Database

SQLite database file: sqlite.db

Tables:

genre(idgenre INTEGER PRIMARY KEY, name VARCHAR(50))

movie(idmovie INTEGER PRIMARY KEY, title VARCHAR(100), release_date DATE, genre_id INTEGER, duration INTEGER, director VARCHAR(100), summary TEXT)

Notes

This project is designed as a learning project to demonstrate JDBC, DAO pattern, and unit testing in Java.

Ensure the .gitignore file ignores target/, IDE files, and OS-specific files like .DS_Store.

Author

Gyamfi Nana Agyemang
