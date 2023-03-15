package com.mythostrike;


import com.mythostrike.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class MythostrikeBackendApplication {

    public static final boolean TEST_MODE = true;

    public static void main(String[] args) {
        SpringApplication.run(MythostrikeBackendApplication.class, args);
    }

}
