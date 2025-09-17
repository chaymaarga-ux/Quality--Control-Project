package com.example.qualitycontrolproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class QualityControlProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(QualityControlProjectApplication.class, args);
    }




    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .requestFactory(() -> {
                    var factory = new HttpComponentsClientHttpRequestFactory();
                    factory.setConnectTimeout(20000);  // // Increase connect timeout 20 secondes
                    factory.setReadTimeout(20000);     //// Increase read timeout 20 secondes
                    return factory;
                })
                .build();
    }
}
