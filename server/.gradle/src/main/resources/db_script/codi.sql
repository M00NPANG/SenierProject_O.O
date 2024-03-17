-- 코디 제작 codicreate 테이블 생성 --

CREATE TABLE codicreate (
                            codi_id INT NOT NULL AUTO_INCREMENT,
                            codiset_id INT NOT NULL,
                            cl_id INT NOT NULL,
                            codi_x INT,
                            codi_y INT,
                            codi_z INT,
                            codi_width INT,
                            codi_height INT,
                            PRIMARY KEY (codi_id),
                            FOREIGN KEY (codiset_id) REFERENCES codiset(codiset_id),
                            FOREIGN KEY (cl_id) REFERENCES clothes(cl_id)
);

-- codiset 테이블 생성 --
CREATE TABLE codiset (
                         codiset_id INT NOT NULL AUTO_INCREMENT,
                         codiset_created_at DATE DEFAULT CURRENT_DATE,
                         codiset_updated_at DATE DEFAULT CURRENT_DATE ON UPDATE CURRENT_DATE,
                         codiset_deleted_at DATE,
                         codiset_columnName VARCHAR(255), -- 컬럼 이름 형식
                         PRIMARY KEY (codiset_id)
);
