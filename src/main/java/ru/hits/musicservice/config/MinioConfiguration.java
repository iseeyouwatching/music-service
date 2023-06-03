package ru.hits.musicservice.config;

import io.minio.MinioClient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Конфигурация MinIO.
 */
@ToString
@Getter
@Setter
@ConfigurationProperties("minio")
public class MinioConfiguration {

    private String url;

    private String accessKey;

    private String secretKey;

    private String bucket;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(url)
                .build();
    }

}
