package teamProject_Server.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "clothes")
public class Clothes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cl_id;

    @OneToOne
    @JoinColumn(name = "user_id") // User 테이블의 기본키를 참조하는 외래키로 지정
    private User user_id;

    private Long cl_category;
    private String cl_brand;
    private String cl_name;
    private Long cl_price;
    private String cl_photo_path;
    private String cl_personal_color;

    // 생성자

    public Clothes() {}

    public Clothes(Long cl_id, User user_id, Long cl_category, String cl_brand, String cl_name, Long cl_price, String cl_photo_path, String cl_personal_color) {
        this.cl_id = cl_id;
        this.user_id = user_id;
        this.cl_category = cl_category;
        this.cl_brand = cl_brand;
        this.cl_name = cl_name;
        this.cl_price = cl_price;
        this.cl_photo_path = cl_photo_path;
        this.cl_personal_color = cl_personal_color;
    }

    //getter and setter

    public Long getCl_id() {
        return cl_id;
    }

    public void setCl_id(Long cl_id) {
        this.cl_id = cl_id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public Long getCl_category() {
        return cl_category;
    }

    public void setCl_category(Long cl_category) {
        this.cl_category = cl_category;
    }

    public String getCl_brand() {
        return cl_brand;
    }

    public void setCl_brand(String cl_brand) {
        this.cl_brand = cl_brand;
    }

    public String getCl_name() {
        return cl_name;
    }

    public void setCl_name(String cl_name) {
        this.cl_name = cl_name;
    }

    public Long getCl_price() {
        return cl_price;
    }

    public void setCl_price(Long cl_price) {
        this.cl_price = cl_price;
    }

    public String getCl_photo_path() {
        return cl_photo_path;
    }

    public void setCl_photo_path(String cl_photo_path) {
        this.cl_photo_path = cl_photo_path;
    }

    public String getCl_personal_color() {
        return cl_personal_color;
    }

    public void setCl_personal_color(String cl_personal_color) {
        this.cl_personal_color = cl_personal_color;
    }
}
