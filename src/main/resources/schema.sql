CREATE TABLE IF NOT EXISTS Users(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    enabled     BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS Authorities(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(255) NOT NULL,
    authority   VARCHAR(255) NOT NULL,
    FOREIGN KEY (username) REFERENCES Users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS UserData(
    username        VARCHAR(255) NOT NULL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    surname         VARCHAR(255) NOT NULL,
    date_of_birth   DATE NOT NULL,
    email           VARCHAR(255) NOT NULL,
    reg_date        DATE NOT NULL,
    FOREIGN KEY (username) REFERENCES Users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS UserUsageStatistics (
    username        VARCHAR(255) NOT NULL,
    program         VARCHAR(255) NOT NULL,
    times           INT NOT NULL,
    PRIMARY KEY(username, program),
    FOREIGN KEY (username) REFERENCES Users(username) ON UPDATE CASCADE ON DELETE CASCADE
);