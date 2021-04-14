package ua.kazo.dentalacademy.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String getResourcesLocation();

    String getResourceHandler();

    String store(MultipartFile file);

    String load(String filename);

    String getPhotoLink(String photoFilename);

    void init();

    void delete(String photoName);

}
