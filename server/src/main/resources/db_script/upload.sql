-- color 테이블 생성 --

CREATE TABLE color (
                       user_id INT NOT NULL ,
                       col_id INT NOT NULL AUTO_INCREMENT,
                       col_red INT,
                       col_yellow INT,
                       col_green INT,
                       col_blue INT,
                       col_orange INT,
                       col_purple INT,
                       col_brown INT,
                       col_gray INT,
                       PRIMARY KEY (col_id),
                       FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- style 테이블 생성 --
CREATE TABLE style (
                       sty_id INT NOT NULL AUTO_INCREMENT,
                       user_id INT NOT NULL,
                       sty_street INT,
                       sty_modern INT,
                       sty_minimal INT,
                       sty_feminine INT,
                       sty_simpleBasic INT,
                       sty_americanCasual INT,
                       sty_businessCasual INT,
                       sty_casual INT,
                       sty_retro INT,
                       sty_sports INT,
                       sty_classic INT,
                       sty_elegant INT,
                       sty_girlish INT,
                       sty_tomboy INT,
                       sty_vintage INT,
                       PRIMARY KEY (sty_id),
                       FOREIGN KEY (user_id) REFERENCES user(user_id)
);


-- userPost 테이블 생성 --

CREATE TABLE userPost (
                          post_id INT NOT NULL AUTO_INCREMENT,
                          user_id INT NOT NULL,
                          post_title VARCHAR(255),
                          post_content VARCHAR(3000),
                          post_created_at DATE DEFAULT CURRENT_DATE,
                          post_deleted_at DATE,
                          post_updated_at DATE,
                          post_views INT DEFAULT 0,
                          post_likes INT DEFAULT 0,
                          PRIMARY KEY (post_id),
                          FOREIGN KEY (user_id) REFERENCES user(user_id)
);
