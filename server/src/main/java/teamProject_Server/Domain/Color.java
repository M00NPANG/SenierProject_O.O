package teamProject_Server.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long col_id;

    private Integer col_red;
    private Integer col_orange;
    private Integer col_yellow;
    private Integer col_green;
    private Integer col_blue;
    private Integer col_purple;
    private Integer col_brown;
    private Integer col_gray; // 회색이라 해놨지만 무채색이라 생각하면 좋을 것 같아용


    @OneToOne
    @JoinColumn(name = "user_id") // User 테이블의 기본키를 참조하는 외래키로 지정
    private User user;

    // 생성자

    public Color() {
    }

    public Color(Integer col_red, Integer col_orange, Integer col_yellow, Integer col_green, Integer col_blue,
                 Integer col_purple, Integer col_brown, Integer col_gray, User user) {
        this.col_red = col_red;
        this.col_orange = col_orange;
        this.col_yellow = col_yellow;
        this.col_green = col_green;
        this.col_blue = col_blue;
        this.col_purple = col_purple;
        this.col_brown = col_brown;
        this.col_gray = col_gray;
        this.user = user;
    }

    // getter and setter

    public Long getCol_id() {
        return col_id;
    }

    public void setCol_id(Long col_id) {
        this.col_id = col_id;
    }

    public Integer getCol_red() {
        return col_red;
    }

    public void setCol_red(Integer col_red) {
        this.col_red = col_red;
    }

    public Integer getCol_orange() {
        return col_orange;
    }

    public void setCol_orange(Integer col_orange) {
        this.col_orange = col_orange;
    }

    public Integer getCol_yellow() {
        return col_yellow;
    }

    public void setCol_yellow(Integer col_yellow) {
        this.col_yellow = col_yellow;
    }

    public Integer getCol_green() {
        return col_green;
    }

    public void setCol_green(Integer col_green) {
        this.col_green = col_green;
    }

    public Integer getCol_blue() {
        return col_blue;
    }

    public void setCol_blue(Integer col_blue) {
        this.col_blue = col_blue;
    }

    public Integer getCol_purple() {
        return col_purple;
    }

    public void setCol_purple(Integer col_purple) {
        this.col_purple = col_purple;
    }

    public Integer getCol_brown() {
        return col_brown;
    }

    public void setCol_brown(Integer col_brown) {
        this.col_brown = col_brown;
    }

    public Integer getCol_gray() {
        return col_gray;
    }

    public void setCol_gray(Integer col_gray) {
        this.col_gray = col_gray;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}