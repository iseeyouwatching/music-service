package ru.hits.musicservice.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hits.musicservice.config.MinioConfiguration;
import ru.hits.musicservice.dto.FileDownloadDto;
import ru.hits.musicservice.entity.FileMetadataEntity;
import ru.hits.musicservice.exception.NotFoundException;
import ru.hits.musicservice.repository.FileMetadataRepository;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;

    private final MinioConfiguration minioConfig;

    private final FileMetadataRepository fileMetadataRepository;

    @PostConstruct
    public void init() {
        log.info("Minio configs: {}", minioConfig);
    }

    @Transactional
    public String upload(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        var id = UUID.randomUUID().toString();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioConfig.getBucket())
                        .object(id)
                        .stream(new ByteArrayInputStream(file.getBytes()), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        FileMetadataEntity fileMetadata = new FileMetadataEntity();
        fileMetadata.setFilename(file.getOriginalFilename());
        fileMetadata.setBucket(minioConfig.getBucket());
        fileMetadata.setObjectName(UUID.fromString(id));
        fileMetadata.setContentType(file.getContentType());
        fileMetadata.setCreationDate(LocalDate.now());
        long fileSize = file.getSize();
        double fileSizeInMB = (double) fileSize / (1024 * 1024);
        fileMetadata.setSize(fileSizeInMB + "MB");
        fileMetadataRepository.save(fileMetadata);

        return id;
    }

    public FileDownloadDto download(UUID id) {
        FileMetadataEntity fileMetadata = fileMetadataRepository.findByObjectName(id)
                .orElseThrow(() -> {
                    String errorMessage = "Файл с ID " + id + " не найден.";
                    log.error(errorMessage);
                    return new NotFoundException(errorMessage);
                });

        var args = GetObjectArgs.builder()
                .bucket(fileMetadata.getBucket())
                .object(String.valueOf(id))
                .build();
        try (var in = minioClient.getObject(args)) {
            return new FileDownloadDto(in.readAllBytes(), fileMetadata.getFilename());
        } catch (Exception e) {
            String errorMessage = "Ошибка при загрузке файла с ID " + id + ".";
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }
}
