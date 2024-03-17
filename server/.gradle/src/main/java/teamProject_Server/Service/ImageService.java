package teamProject_Server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import teamProject_Server.Domain.Image;
import teamProject_Server.Repository.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {


    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Value("${upload.directory}") // application.properties에서 주입받음
    private String uploadDirectoryUrl;

    public String saveImage(String userEmail, String category, MultipartFile file) {
        // 새로운 파일 이름 생성
        String fileName = generateFileName(userEmail, category, file);

        // 파일이 저장될 주소 생성
        String directoryPath = generateDirectoryPath(userEmail, category);

        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 파일 경로
            String filePath = directoryPath + fileName;
            file.transferTo(new File(filePath));

            // 이미지 정보를 DB에 저장
            Image image = new Image(fileName, file.getOriginalFilename(), filePath);
            imageRepository.save(image);

            return filePath;

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("파일 저장 중 오류가 발생했습니다.");
        }
    }

    // 저장될 파일 이름 재설정
    private String generateFileName(String userEmail, String category, MultipartFile file) {
        String userName = userEmail.split("@")[0]; // 이메일에서 @ 앞 부분 추출
        String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        return userName + "_" + category + "_" + UUID.randomUUID().toString() + extension;
    }

    // 저장될 파일 URL 재설정
    private String generateDirectoryPath(String userEmail, String category) {
        String userName = userEmail.split("@")[0]; // 이메일에서 @ 앞 부분 추출
        return uploadDirectoryUrl + userName + "\\" + category + "\\";
    }
}