package teamProject_Server.Domain;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Getter;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;       // 추천 게시물 id
    private String hashtag;     // 이거 삭제할지 확인 필요
    private String userEmail;   // 유저 분류를 위한 id 대체

    private Long top_id;            // 상의 id
    private Long bottom_id;         // 하의 id
    private Long shoes_id;          // 신발 id
    private Long accessories_id;    // 악세서리 id
    private Long bag_id;            // 가방 id

    // 생성자
    public Post() {}

    public Post(String hashtag, String userEmail, Long top_id, Long bottom_id, Long shoes_id, Long accessories_id, Long bag_id) {
        this.hashtag = hashtag;
        this.userEmail = userEmail;
        this.top_id = top_id;
        this.bottom_id = bottom_id;
        this.shoes_id = shoes_id;
        this.accessories_id = accessories_id;
        this.bag_id = bag_id;
    }

    // getter and setter

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getTop_id() {
        return top_id;
    }

    public void setTop_id(Long top_id) {
        this.top_id = top_id;
    }

    public Long getBottom_id() {
        return bottom_id;
    }

    public void setBottom_id(Long bottom_id) {
        this.bottom_id = bottom_id;
    }

    public Long getShoes_id() {
        return shoes_id;
    }

    public void setShoes_id(Long shoes_id) {
        this.shoes_id = shoes_id;
    }

    public Long getAccessories_id() {
        return accessories_id;
    }

    public void setAccessories_id(Long accessories_id) {
        this.accessories_id = accessories_id;
    }

    public Long getBag_id() {
        return bag_id;
    }

    public void setBag_id(Long bag_id) {
        this.bag_id = bag_id;
    }
}

