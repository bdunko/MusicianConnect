# -----------------------------------------------
# SQL script to create the MusicianConnectDB database
# tables and populate the PublicSongs table.
# Created by Christopher Koehler & Ben Dunko
# -----------------------------------------------

/*
Tables to be dropped must be listed in a logical order based on dependency.
PlayableSong, WishList, and UserPhoto depend on User. Therefore, they must be dropped before User.
*/
DROP TABLE IF EXISTS Friend, PublicSong, PlayableSong, WishList, UserPhoto, User, Video;

CREATE TABLE Video
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    title VARCHAR (512) NOT NULL,
    description VARCHAR (2048) NOT NULL,
    youtube_video_id VARCHAR (32) NOT NULL,
    category VARCHAR (32) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL,
    password VARCHAR(256) NOT NULL,          /* To store Salted and Hashed Password Parts */
    first_name VARCHAR(32) NOT NULL,
    middle_name VARCHAR(32),
    last_name VARCHAR(32) NOT NULL,
    address1 VARCHAR(128) NOT NULL,
    address2 VARCHAR(128),
    city VARCHAR(64) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zipcode VARCHAR(10) NOT NULL,            /* e.g., 24060-1804 */
    security_question_number INT NOT NULL,   /* Refers to the number of the selected security question */
    security_answer VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL,      
    experience VARCHAR(32) NOT NULL,
    instruments_played VARCHAR(128) NOT NULL,
    favorite_genre VARCHAR(32) NOT NULL,      
    language VARCHAR(32) NOT NULL,     
    about_me VARCHAR(512) NOT NULL, 
    PRIMARY KEY (id)
);

CREATE TABLE UserPhoto
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    extension ENUM('jpeg', 'jpg', 'png', 'gif') NOT NULL,
    user_id INT UNSIGNED,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE WishList
(
    id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id INT UNSIGNED,
    title VARCHAR (512) NOT NULL,
    artist VARCHAR (512) NOT NULL,
    genre VARCHAR (64) NOT NULL,
    duration VARCHAR (8),
    lyrics VARCHAR (8000),
    tabs_link VARCHAR (512),
    youtube_video_id VARCHAR (32) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE PlayableSong
(
    id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id INT UNSIGNED,
    title VARCHAR (512) NOT NULL,
    artist VARCHAR (512) NOT NULL,
    genre VARCHAR (64) NOT NULL,
    duration VARCHAR (8),
    lyrics VARCHAR (8000),
    tabs_link VARCHAR (512),
    youtube_video_id VARCHAR (32) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE PublicSong
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    title VARCHAR (512) NOT NULL,
    artist VARCHAR (512) NOT NULL,
    genre VARCHAR (64) NOT NULL,
    duration VARCHAR (8),
    lyrics VARCHAR (8000),	
    tabs_link VARCHAR (512),	
    youtube_video_id VARCHAR (32) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Message
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id INT UNSIGNED,
    message VARCHAR(4000) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Friend
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id INT UNSIGNED,			
    username VARCHAR(32) NOT NULL,
    first_name VARCHAR(32) NOT NULL,
    last_name VARCHAR(32) NOT NULL,
    is_friend INT UNSIGNED,
    my_id INT UNSIGNED,
	active INT UNSIGNED,
	my_first_name  VARCHAR(32) NOT NULL,
	my_last_name  VARCHAR(32) NOT NULL,
	my_username VARCHAR(32) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);
