package teamProject_Server.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "userpost")
public class UserPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @OneToOne
    @JoinColumn(name = "user_id") // User 테이블의 기본키를 참조하는 외래키로 지정
    private User user_id;

    private String post_title;  // 게시물 제목
    private String post_content;        // 게시물 내용
    private String post_created_at;     // 게시물 제작 시간
    private String post_deleted_at;     // 게시물 삭제 시간
    private String post_updated_at;     // 게시물 수정 시간
    private Long post_likes;            // 게시물 좋아요 수
    private String post_color;          // 색 모음
    private String post_img;            // 이미지 주소


    @JoinColumn(name = "user_img") // User 테이블의 유저이미지를 참조하는 외래키로 지정
    private String user_img;

    // 바이트 변환을 위한 임시 저장 배열
    @Transient
    private byte[] postImageBytes; // 이미지 바이트 데이터를 저장할 필드


    @Transient  // 데이터베이스에 매핑되지 않는 필드임을 나타냅니다.
    private byte[] userImageBytes;


    // 생성자
    public UserPost() {}

    public UserPost(User user_id, String post_title, String post_content, String post_created_at, String post_deleted_at, String post_updated_at, Long post_likes, String post_color, String post_img, String user_img) {
        this.user_id = user_id;
        this.post_title = post_title;
        this.post_content = post_content;
        this.post_created_at = post_created_at;
        this.post_deleted_at = post_deleted_at;
        this.post_updated_at = post_updated_at;
        this.post_likes = post_likes;
        this.post_color = post_color;
        this.post_img = post_img;
        this.user_img = user_img;
    }




    // getter and setter


    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_created_at() {
        return post_created_at;
    }

    public void setPost_created_at(String post_created_at) {
        this.post_created_at = post_created_at;
    }

    public String getPost_deleted_at() {
        return post_deleted_at;
    }

    public void setPost_deleted_at(String post_deleted_at) {
        this.post_deleted_at = post_deleted_at;
    }

    public String getPost_updated_at() {
        return post_updated_at;
    }

    public void setPost_updated_at(String post_updated_at) {
        this.post_updated_at = post_updated_at;
    }

    public Long getPost_likes() {
        return post_likes;
    }

    public void setPost_likes(Long post_likes) {
        this.post_likes = post_likes;
    }

    public String getPost_color() {
        return post_color;
    }

    public void setPost_color(String post_color) {
        this.post_color = post_color;
    }

    public String getPost_img() {
        return post_img;
    }

    public void setPost_img(String post_img) {
        this.post_img = post_img;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public byte[] getPostImageBytes() {
        return postImageBytes;
    }

    public void setPostImageBytes(byte[] postImageBytes) {
        this.postImageBytes = postImageBytes;
    }

    // userImageBytes 필드에 대한 getter 메서드
    public byte[] getUserImageBytes() {
        return userImageBytes;
    }

    // userImageBytes 필드에 대한 setter 메서드
    public void setUserImageBytes(byte[] userImageBytes) {
        this.userImageBytes = userImageBytes;
    }
}
