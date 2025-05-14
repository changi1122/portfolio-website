package net.studio1122.changi1122.portfoliowebsite.global;

import net.studio1122.changi1122.portfoliowebsite.domain.common.UploadType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Configuration
public class S3Config {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.username}")
    private String username;

    @Value("${minio.password}")
    private String password;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of("ap-northeast-cju-1"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(username, password)
                ))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build()
                )
                .build();
    }
}
