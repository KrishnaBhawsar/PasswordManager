package com.pwdmgr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${client.url}")
    private String clientUrl;

    public void addCorsMappings(CorsRegistry registry) {
        System.out.println(clientUrl);
        registry.addMapping("/**")
                .allowedOrigins(clientUrl, "http://localhost:3000") // Update with your client's URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
