package teamProject_Server.Domain;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Getter;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;
    private String hashtag;
    private String content;
    private String title;
    private String imagePath;
    private String userName;
    private String userEmail;

    public Post(){}
    public Post(String hashtag, String content, String title, String imagePath, String userEmail,String userName) {
        this.hashtag = hashtag;
        this.content = content;
        this.title = title;
        this.imagePath = imagePath;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getHashtag() { return hashtag; }
    public void setHashtag(String hashtag) { this.hashtag = hashtag; }

    public void setContent(String content) { this.content = content; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String useremail) { this.userEmail = useremail; }

    public String getUserName() { return userName; }
    public void setUserName(String username) { this.userName = username; }






}

