package teamProject_Server.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Repository
public interface FileStorageService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
}

