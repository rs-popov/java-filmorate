MERGE INTO mpa_rating KEY (mpa_id, mpa_name) VALUES (1, 'G');
MERGE INTO mpa_rating KEY (mpa_id, mpa_name) VALUES (2, 'PG');
MERGE INTO mpa_rating KEY (mpa_id, mpa_name) VALUES (3, 'PG-13');
MERGE INTO mpa_rating KEY (mpa_id, mpa_name) VALUES (4, 'R');
MERGE INTO mpa_rating KEY (mpa_id, mpa_name) VALUES (5, 'NC-17');

MERGE INTO genres KEY (genre_id, genre_name) VALUES (1, 'Комедия');
MERGE INTO genres KEY (genre_id, genre_name) VALUES (2, 'Драма');
MERGE INTO genres KEY (genre_id, genre_name) VALUES (3, 'Мультфильм');
MERGE INTO genres KEY (genre_id, genre_name) VALUES (4, 'Фантастика');
MERGE INTO genres KEY (genre_id, genre_name) VALUES (5, 'Ужасы');
MERGE INTO genres KEY (genre_id, genre_name) VALUES (6, 'Мелодрама');