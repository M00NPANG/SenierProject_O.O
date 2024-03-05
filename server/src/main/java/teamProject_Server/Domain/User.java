package teamProject_Server.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String password;
    private String email;
    private String name;
    private String user_percol;
    private String imgPath;

    // 생성자

    public User() {}

    public User(String email, String password, String name, String user_percol, String imgPath) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.user_percol = user_percol;
        this.imgPath = imgPath;
    }

    // Getter 및 Setter

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_percol() { return user_percol; }

    public void setUser_percol(String user_percol) { this.user_percol = user_percol; }

    public String getImgPath() { return imgPath; }

    public void setImgPath(String imgPath) { this.imgPath = imgPath; }
}
