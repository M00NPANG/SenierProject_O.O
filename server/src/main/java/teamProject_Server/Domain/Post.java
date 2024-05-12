package teamProject_Server.Domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

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


    // 해시태그를 JSON으로 설정
    // JSON 형태의 해시태그 문자열을 설정하는 메서드
    public void setHashtags(List<String> hashtags) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.hashtag = mapper.writeValueAsString(hashtags);
    }

    // JSON 형태의 해시태그 문자열을 List<String>으로 변환하여 반환하는 메서드
    public List<String> getHashtags() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String[] tagsArray = mapper.readValue(this.hashtag , String[].class);
        return Arrays.asList(tagsArray);
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String useremail) { this.userEmail = useremail; }

    public String getUserName() { return userName; }
    public void setUserName(String username) { this.userName = username; }






}

