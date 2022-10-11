# --- !Ups

CREATE TABLE "PILL" (
    "ID" INT NOT NULL auto_increment primary key,
    "TEXT" varchar(255) NOT NULL,
    "USER_ID" INT NOT NULL,
    "REPLY_ID" INT,
    "DATE" VARCHAR NOT NULL
);

CREATE TABLE "USER" (
    "ID" INT NOT NULL AUTO_INCREMENT primary key,
    "USERNAME" VARCHAR(255) NOT NULL,
    "PROVIDER_ID" VARCHAR(255) NOT NULL,
    "PROVIDER_KEY" VARCHAR(255) NOT NULL
);

CREATE TABLE "PILL_LIKE" (
    "PILL_ID" INT NOT NULL,
    "USER_ID" INT NOT NULL,
    PRIMARY KEY ("PILL_ID", "USER_ID")
);

CREATE TABLE "PASSWORD" (
    "PROVIDER_KEY" VARCHAR(255) NOT NULL primary key,
    "HASHER" VARCHAR(255) NOT NULL,
    "HASH" VARCHAR(255) NOT NULL,
    "SALT" VARCHAR(255)
);

# --- !Downs

DROP TABLE "PILL";
DROP TABLE "USER";
DROP TABLE "PILL_LIKE"
DROP TABLE "PASSWORD";