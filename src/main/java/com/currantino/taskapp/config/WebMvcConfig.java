package com.currantino.taskapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //TODO: Add secure cors
        registry.addMapping("/**")
                .allowedOriginPatterns("http://*:[*]")
                .allowedHeaders("*")
                .allowedMethods("*");
    }
}