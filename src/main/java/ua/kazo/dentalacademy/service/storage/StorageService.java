package ua.kazo.dentalacademy.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {

    void init();

    String store(MultipartFile file);

    Path load(String filename);

    Resource loadAsResource(String filename);

}
