package com.example.demo.config;

import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer, Filter {

    @Autowired
    private AuthService authService;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    ArrayList<String> AllowedOrigins;
    ArrayList<String> AllowedMethods;

    @Override
    public void doFilter(ServletRequest req , ServletResponse res , FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (request.getMethod().equals("OPTIONS")) {
            String reqOrigin;
            reqOrigin = request.getHeader("Origin");

            for (String origin : AllowedOrigins) {
                if (origin.equals(reqOrigin)) {
                    response.setHeader("Access-Control-Allow-Origin" , reqOrigin);
                    break;
                }
            }

            String reqMethod;
            reqMethod = request.getHeader("Access-Control-Request-Method");

            for (String method : AllowedMethods) {
                if (method.equals(reqMethod)) {
                    response.setHeader("Access-Control-Allow-Method" , reqMethod);
                }
            }

            response.setHeader("Access-Control-Max-Age" , "3600");
            response.setHeader("Access-Control-Allow-Credentials" , "true");
            response.setHeader("Access-Control-Allow-Headers" , "cache-control,if-modified-since,pragma,Content-Type");

            //Checks if Allow-Method and Allow-Origin got set. 200 OK if both are set.
            if (!response.getHeader("Access-Control-Allow-Method").equals("") && !response.getHeader("Access-Control-Allow-Origin").equals("")) {
                response.setStatus(200);
            }
        } else {
            filterChain.doFilter(req , res);
        }
    }
}
