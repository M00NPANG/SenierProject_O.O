package teamProject_Server.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long img_id;

    private String fileName;
    private String fileOriName;
    private String fileUrl;

    // 생성자
    public Image() {};

    public Image(String fileName, String fileOriName, String fileUrl) {
        this.fileName = fileName;
        this.fileOriName = fileOriName;
        this.fileUrl = fileUrl;
    }

    // getter and setter
    public Long getImg_id() { return img_id; }

    public void setImg_id(Long img_id) { this.img_id = img_id; }

    public String getFileName() { return fileName; }

    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileOriName() { return fileOriName; }

    public void setFileOriName(String fileOriName) { this.fileOriName = fileOriName; }

    public String getFileUrl() { return fileUrl; }

    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
}
