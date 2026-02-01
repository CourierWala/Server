//package com.courierwala.server.config;
//
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(List.of("http://localhost:80","http://3.7.13.235:80","http://3.7.13.235",
//                "http://3.7.13.235:5173"));
//
//        config.setAllowedMethods(
//                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//        );
//
//        
//        config.setAllowedHeaders(List.of("*"));
//        config.setExposedHeaders(List.of("Set-Cookie"));
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }
//}
