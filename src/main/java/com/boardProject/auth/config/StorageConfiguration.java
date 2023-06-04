package com.boardProject.auth.config;

import com.boardProject.board.storageService.FileSystemStorageService;
import com.boardProject.board.storageService.S3StorageService;
import com.boardProject.board.storageService.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class StorageConfiguration {
    private static final String REGION = "ap-northeast-2";
    private final Environment env;

    public StorageConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public StorageService fileSystemStorageService() {
        return new FileSystemStorageService();
    }

    @Primary
    @Bean
    public StorageService s3StorageService() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(env.getProperty("cloud.s3.access-id"), env.getProperty("cloud.s3.secret-key"));


        S3Client s3Client =
                S3Client.builder()
                        .region(Region.of(REGION))
                        .credentialsProvider(StaticCredentialsProvider.create(credentials))
                        .build();
        return new S3StorageService(s3Client);
    }
}
