package ua.kazo.dentalacademy.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ua.kazo.dentalacademy.properties.StorageProperties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileSystemStorageService extends AbstractStorageService {

    private final StorageProperties storageProperties;
    private final Path rootLocation;

    public FileSystemStorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
        this.rootLocation = Paths.get(storageProperties.getLocation());
    }

    @Override
    public String getResourcesLocation() {
        return "file:" + storageProperties.getLocation() + "\\";
    }

    @Override
    public String getResourceHandler() {
        return "/files/**";
    }

    protected Path getDestinationFile(String newFilename) {
        Path destinationFile = this.rootLocation
                .resolve(Paths.get(newFilename))
                .normalize()
                .toAbsolutePath();
        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            // This is a security check
            throw new StorageException("Cannot store file outside current directory.");
        }
        return destinationFile;
    }

    @Override
    public String load(String filename) {
        return rootLocation.resolve(filename).toString();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    protected String copyFileToServer(InputStream inputStream, String newFileName) {
        try {
            return String.valueOf(Files.copy(inputStream, getDestinationFile(newFileName)));
        } catch (IOException e) {
            throw new StorageException("Failed to store file: " + e.getMessage());
        }
    }

    @Override
    public String getPhotoLink(String photoFilename) {
        return MvcUriComponentsBuilder
                .fromMethodName(FileUploadController.class, "serveFile",
                        Path.of(load(photoFilename)).getFileName().toString())
                .build()
                .toUri()
                .toString();
    }

    @Override
    public void delete(String photoName) {
        Path rootLocation = Paths.get(storageProperties.getLocation());
        Path fileToDelete = rootLocation.resolve(Paths.get(photoName))
                .normalize()
                .toAbsolutePath();
        try {
            Files.delete(fileToDelete);
        } catch (NoSuchFileException ex) {
            log.warn("No such file or directory: {}", fileToDelete);
        } catch (IOException ex) {
            log.warn("File: {}, permission problems: {}", fileToDelete, ex.getMessage());
        }
    }

}
