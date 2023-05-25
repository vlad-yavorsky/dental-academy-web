package ua.kazo.dentalacademy.service.storage;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractStorageService implements StorageService {

    private String fileNameChange(String originalFilename) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return now.format(formatter) + "_" + DigestUtils.md5Hex(originalFilename + now.getNano()) + "." + FilenameUtils.getExtension(originalFilename);
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            String newFilename = fileNameChange(file.getOriginalFilename());
            try (InputStream inputStream = file.getInputStream()) {
                copyFileToServer(inputStream, newFilename);
                return newFilename;
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    protected abstract String copyFileToServer(InputStream inputStream, String newFileName);

}
