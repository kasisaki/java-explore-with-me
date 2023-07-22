CREATE TABLE IF NOT EXISTS stats_data
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app     VARCHAR(255) NOT NULL,
    ip      VARCHAR(255) NOT NULL,
    uri     VARCHAR(255) NOT NULL,
    created TIMESTAMP    NOT NULL
);