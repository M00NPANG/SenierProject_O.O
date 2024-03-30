-- Database Name --

CREATE DATABASE outfitoracle;

USE outfitoracle;



-- USER TABLE STRUCTURE good--
CREATE TABLE user (
  user_id INT NOT NULL AUTO_INCREMENT,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (user_id)
);

-- 이미지 테이블 생성 --
CREATE TABLE image (
    fileName VARCHAR(255) NOT NULL,
    fileOriName VARCHAR(255) NOT NULL,
    fileUrl VARCHAR(255) NOT NULL,
    PRIMARY KEY (fileName)
);

