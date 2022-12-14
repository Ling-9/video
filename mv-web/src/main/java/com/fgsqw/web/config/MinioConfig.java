package com.fgsqw.web.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.access-key}")
    private String accessKey;
    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient(){
        MinioClient minioClient = null;
        try {
            minioClient = new MinioClient(endpoint,accessKey,secretKey);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("创建minio对象失败");
        }
        return minioClient;
    }
}
