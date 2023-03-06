package com.mythostrike;


import com.mythostrike.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableAsync
public class MythostrikeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MythostrikeBackendApplication.class, args);
    }

}
