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

CREATE TABLE IF NOT EXISTS Exercises(
    name            VARCHAR(255) NOT NULL,
    sets            INT NOT NULL,
    reps            INT NOT NULL,
    kcal            INT NOT NULL,
    program_name    VARCHAR(255) NOT NULL,
    PRIMARY KEY (name, program_name)
);


CREATE TABLE IF NOT EXISTS UserUsageStatistics (
    username        VARCHAR(255) NOT NULL,
    program         VARCHAR(255) NOT NULL,
    times           INT NOT NULL,
    PRIMARY KEY(username, program),
    FOREIGN KEY (username) REFERENCES Users(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Reviews(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(255) NOT NULL, -- Username non è chiave esterna per mantenere le recensioni quando viene cancellato l'account
    review          TEXT NOT NULL
);