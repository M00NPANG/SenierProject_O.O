package teamProject_Server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import teamProject_Server.Domain.Image;
<<<<<<< HEAD
import teamProject_Server.Repository.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
=======
import teamProject_Server.Domain.User;
import teamProject_Server.Repository.ImageRepository;
import teamProject_Server.Repository.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
>>>>>>> 4024be3d62498ce9be23883e92c8a8137a97c09f
import java.util.UUID;

@Service
public class ImageService {


    private final ImageRepository imageRepository;
<<<<<<< HEAD

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
=======
    private final UserRepository userRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
>>>>>>> 4024be3d62498ce9be23883e92c8a8137a97c09f
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
<<<<<<< HEAD
            String filePath = directoryPath + fileName;
            file.transferTo(new File(filePath));

            // 이미지 정보를 DB에 저장
            Image image = new Image(fileName, file.getOriginalFilename(), filePath);
            imageRepository.save(image);

            return filePath;
=======
            String fileUrl = directoryPath + fileName;
            file.transferTo(new File(fileUrl));

            // 이미지 정보를 DB에 저장
            Image image = new Image(fileName, file.getOriginalFilename(), fileUrl);
            imageRepository.save(image);

            return fileUrl;
>>>>>>> 4024be3d62498ce9be23883e92c8a8137a97c09f

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("파일 저장 중 오류가 발생했습니다.");
        }
    }

<<<<<<< HEAD
    // 저장될 파일 이름 재설정
=======
    // 이미지 파일을 byte 배열로 읽어서 반환하는 메소드
    public byte[] loadImageAsBytes(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }

    // 저장될 파일 이름 재설정(아이디_카테고리_UUID.확장자)
>>>>>>> 4024be3d62498ce9be23883e92c8a8137a97c09f
    private String generateFileName(String userEmail, String category, MultipartFile file) {
        String userName = userEmail.split("@")[0]; // 이메일에서 @ 앞 부분 추출
        String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        return userName + "_" + category + "_" + UUID.randomUUID().toString() + extension;
    }

<<<<<<< HEAD
    // 저장될 파일 URL 재설정
=======
    // 저장될 파일 URL 재설정(아이디\\카테고리)
>>>>>>> 4024be3d62498ce9be23883e92c8a8137a97c09f
    private String generateDirectoryPath(String userEmail, String category) {
        String userName = userEmail.split("@")[0]; // 이메일에서 @ 앞 부분 추출
        return uploadDirectoryUrl + userName + "\\" + category + "\\";
    }
}