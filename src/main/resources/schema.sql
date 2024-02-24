DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS film_genre CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friendship CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS genre CASCADE;

CREATE TABLE IF NOT EXISTS mpa (
mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
mpa_name varchar);

CREATE TABLE IF NOT EXISTS genre (
genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
genre_name varchar unique );

CREATE TABLE IF NOT EXISTS film (
film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
film_name varchar,
description varchar,
release_date date,
duration integer,
mpa_id integer REFERENCES mpa (mpa_id) ON DELETE CASCADE,
genre_id integer REFERENCES genre (genre_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS film_genre (
film_id INTEGER REFERENCES film (film_id) ON DELETE CASCADE,
genre_id INTEGER REFERENCES genre (genre_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS users (
user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email varchar,
login varchar,
user_name varchar,
birthday date);

CREATE TABLE IF NOT EXISTS likes (
user_id integer REFERENCES users (user_id) ON DELETE CASCADE,
film_id varchar REFERENCES film (film_id) ON DELETE CASCADE);

CREATE TABLE if not exists friendship (
user_id integer REFERENCES users (user_id) ON DELETE CASCADE,
friend_id integer REFERENCES users (user_id) ON DELETE CASCADE,
status boolean);


