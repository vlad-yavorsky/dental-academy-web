package ua.kazo.dentalacademy.service.storage;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import ua.kazo.dentalacademy.properties.AppProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class S3StorageService extends AbstractStorageService {

    private final S3Client s3;
    private final AppProperties appProperties;

    @Override
    public String getResourcesLocation() {
        return "/s3://" + appProperties.getAws().getBucketName() + "/";
    }

    @Override
    public String getResourceHandler() {
        return "/s3/**";
    }

    @SneakyThrows
    @Override
    public String load(String filename) {
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(appProperties.getAws().getBucketName())
                .key(filename)
                .build();
        return s3.utilities().getUrl(request).toString();
    }

    @Override
    public void init() {
        // do nothing
    }

    @Override
    public void delete(String photoName) {
        try {
            List<ObjectIdentifier> toDelete = List.of(ObjectIdentifier.builder().key(photoName).build());
            DeleteObjectsRequest dor = DeleteObjectsRequest.builder()
                    .bucket(appProperties.getAws().getBucketName())
                    .delete(Delete.builder().objects(toDelete).build())
                    .build();
            s3.deleteObjects(dor);
        } catch (S3Exception e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
    }

    @Override
    protected String copyFileToServer(InputStream inputStream, String newFileName) {
        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(appProperties.getAws().getBucketName())
                    .key(newFileName)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            PutObjectResponse response = s3.putObject(putOb, RequestBody.fromBytes(inputStream.readAllBytes()));
            return response.eTag();
        } catch (S3Exception | IOException e) {
            log.error(e.getMessage());
        }
        return "";
    }

    @Override
    public String getPhotoLink(String photoFilename) {
        return load(photoFilename);
    }

}
