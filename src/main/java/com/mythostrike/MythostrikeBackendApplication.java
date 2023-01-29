package com.mythostrike;


import com.mythostrike.account.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class MythostrikeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MythostrikeBackendApplication.class, args);
    }

}
