DROP TABLE IF EXISTS film_genres, likes, friendship, films, genres, mpa_rating, users;
CREATE TABLE IF NOT EXISTS genres
(
    genre_id   INTEGER PRIMARY KEY,
    genre_name varchar(30)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id       INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_email    varchar(100),
    user_login    varchar(100),
    user_name     varchar(100),
    user_birthday date
);

CREATE TABLE IF NOT EXISTS mpa_rating
(
    mpa_id   INTEGER PRIMARY KEY,
    mpa_name varchar(10)
);

CREATE TABLE IF NOT EXISTS films
(
    film_id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_name        varchar(100),
    film_description varchar(200),
    film_releaseDate date,
    film_duration    INTEGER,
    film_mpa_id      INTEGER,
    CONSTRAINT fk_films_film_mpa_id FOREIGN KEY (film_mpa_id) REFERENCES mpa_rating (mpa_id)
);



CREATE TABLE IF NOT EXISTS friendship
(
    user_id   INTEGER,
    friend_id INTEGER,
    status    varchar,
    CONSTRAINT pk_friendship PRIMARY KEY (user_id, friend_id),
    CONSTRAINT fk_friendship_user_id FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_friendship_friend_id FOREIGN KEY (friend_id) REFERENCES users (user_id)
);



CREATE TABLE IF NOT EXISTS film_genres
(
    film_id  INTEGER,
    genre_id INTEGER,
    CONSTRAINT pk_film_genres PRIMARY KEY (film_id, genre_id),
    CONSTRAINT fk_film_genres_film_id FOREIGN KEY (film_id) REFERENCES films (film_id),
    CONSTRAINT fk_film_genres_genre_id FOREIGN KEY (genre_id) REFERENCES genres (genre_id)
);

CREATE TABLE IF NOT EXISTS likes
(
    film_id INTEGER,
    user_id INTEGER,
    CONSTRAINT pk_likes PRIMARY KEY (film_id, user_id),
    CONSTRAINT fk_likes_film_id FOREIGN KEY (film_id) REFERENCES films (film_id),
    CONSTRAINT fk_likes_user_id FOREIGN KEY (user_id) REFERENCES users (user_id)
);
