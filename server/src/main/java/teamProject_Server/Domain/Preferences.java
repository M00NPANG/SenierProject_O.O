package teamProject_Server.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "preferences")
public class Preferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pre_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "json")
    private String preferences; // JSON 형식의 선호도 정보를 문자열로 저장

    // 생성자
    public Preferences() {}

    public Preferences(User user, String preferences) {
        this.user = user;
        this.preferences = preferences;
    }

    // getter and setter

    public Long getPre_id() {
        return pre_id;
    }

    public void setPre_id(Long pre_id) {
        this.pre_id = pre_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
