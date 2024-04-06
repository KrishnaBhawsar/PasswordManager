package com.pwdmgr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${client.url.1}")
    private String clientUrl1;
    @Value("&{client.url.2}")
    private String clientUrl2;

    public void addCorsMappings(CorsRegistry registry) {
        System.out.println(clientUrl1+ clientUrl2);
        registry.addMapping("/**")
                .allowedOrigins(clientUrl1, clientUrl2) // Update with your client's URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
