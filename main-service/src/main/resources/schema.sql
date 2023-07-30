CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name    VARCHAR(255) NOT NULL,
    email   VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user_id PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    CONSTRAINT pk_category_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    lon FLOAT,
    lat FLOAT,
    CONSTRAINT pk_location_id PRIMARY KEY (id),
    CONSTRAINT UQ_LOCATION UNIQUE (lon, lat)
);

CREATE TABLE IF NOT EXISTS events
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY,
    annotation        VARCHAR(2000) NOT NULL,
    category_id       BIGINT        REFERENCES categories (id) ON DELETE SET NULL,
    description       VARCHAR(7000),
    created_on        TIMESTAMP     NOT NULL,
    event_date        TIMESTAMP     NOT NULL,
    initiator_id      BIGINT REFERENCES users (id) ON DELETE SET NULL,
    paid              BOOLEAN,
    location_id       BIGINT        REFERENCES locations (id) ON DELETE SET NULL,
    participant_limit INTEGER       NOT NULL,
    requestModeration BOOLEAN       NOT NULL,
    title             VARCHAR(50),
    CONSTRAINT pk_item_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY,
    description  VARCHAR(1000) NOT NULL,
    requestor_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    created      TIMESTAMP WITHOUT TIME ZONE,
    item_id      BIGINT        REFERENCES events (id) ON DELETE SET NULL,
    CONSTRAINT pk_request_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY,
    pinned BOOLEAN DEFAULT FALSE,
    title  VARCHAR(50) NOT NULL,
    CONSTRAINT pk_compilation_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_of_events
(
    event_id       BIGINT REFERENCES events (id) ON DELETE CASCADE,
    compilation_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE,
    CONSTRAINT pk_event_comp PRIMARY KEY (event_id, compilation_id)
);