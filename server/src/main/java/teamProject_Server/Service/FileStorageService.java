package teamProject_Server.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
}

