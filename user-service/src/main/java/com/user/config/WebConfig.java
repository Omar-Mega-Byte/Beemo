// package com.user.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.lang.NonNull;
// import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer {
//     @Override
//     public void addViewControllers(@NonNull ViewControllerRegistry registry) {
//         // Map /login and /register (no .html) to their static HTML files
//         registry.addViewController("/login").setViewName("forward:/login.html");
//         registry.addViewController("/register").setViewName("forward:/register.html");
//     }
// }
