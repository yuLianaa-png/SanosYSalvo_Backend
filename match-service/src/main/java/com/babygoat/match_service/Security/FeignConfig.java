package com.babygoat.match_service.Security;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String authToken = attributes.getRequest().getHeader("Authorization");
                if (authToken != null) {
                    System.out.println("DEBUG: Propagando token al Pet-Service...");
                    requestTemplate.header("Authorization", authToken);
                } else {
                    System.out.println("DEBUG: No se encontró el header Authorization en la petición actual");
                }
            }
        };
    }
}
