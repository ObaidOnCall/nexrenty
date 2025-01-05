package com.nexrenty.clients_service.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.minio.MinioAsyncClient;
import io.minio.MinioClient;

@Configuration
public class UtilsConf {
    


    @Bean
    public MinioClient minioClient () {

        return MinioClient.builder()
                .endpoint("https://storage-api.obayd.online")
                .credentials("609RmPWnisHxBxIDirOx", "ANfl7eLcy7sayPibJIE2QAivf5c4rO86H3Rvua5e")
                .build();
    }

    @Bean
    public MinioAsyncClient minioAsyncClient () {

        return MinioAsyncClient.builder()
                .endpoint("https://storage-api.obayd.online")
                .credentials("DcLfYpiX4FK1u", "akgSNqmV9IE8wR3pC70AX0j71Si")
                .build();
    }
}