CREATE
EXTENSION postgis;

CREATE DATABASE fly_db;
CREATE DATABASE hotel_db;
CREATE DATABASE account_db;

\c fly_db;

CREATE TABLE fly_booking
(
    id          SERIAL       NOT NULL,
    client_name VARCHAR(50)  NOT NULL,
    fly_number  VARCHAR(255) DEFAULT NULL,
    fromdest        VARCHAR(255) NOT NULL,
    todest          VARCHAR(255) NOT NULL,
    date        timestamp(0) WITHOUT TIME ZONE DEFAULT NULL,
    PRIMARY KEY (id)
);


\c hotel_db;
CREATE TABLE hotel_booking
(
    id          SERIAL      NOT NULL,
    client_name VARCHAR(50) NOT NULL,
    hotel_name  VARCHAR(255) DEFAULT NULL,
    arrival     timestamp(0) WITHOUT TIME ZONE DEFAULT NULL,
    departure   timestamp(0) WITHOUT TIME ZONE DEFAULT NULL,
    PRIMARY KEY (id)
);

\c account_db;
CREATE TABLE account
(
    id          SERIAL       NOT NULL,
    client_name VARCHAR(255) NOT NULL,
    amount      numeric CHECK (amount > 0)
);
