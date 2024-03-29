CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT,
    name_ru VARCHAR(128),
    name_en VARCHAR(128),
    film_year INT,
    description TEXT,
    rating VARCHAR(16),
    url TEXT,
    CONSTRAINT pk_films PRIMARY KEY (film_id)
);

CREATE TABLE IF NOT EXISTS users (
	chat_id BIGINT,
	username VARCHAR(128),
	CONSTRAINT pk_users PRIMARY KEY (chat_id)
);

CREATE TABLE IF NOT EXISTS user_films (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    chat_id BIGINT REFERENCES users (chat_id) ON DELETE CASCADE,
    film_id BIGINT REFERENCES films (film_id),
    add_date TIMESTAMP WITHOUT TIME ZONE
)