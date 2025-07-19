// package com.user.controller;

// import java.io.IOException;

// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;

// import jakarta.servlet.http.HttpServletResponse;

// @Controller
// public class StaticController {
//     @GetMapping("/users/login")
//     public void redirectToLoginHtml(HttpServletResponse response) throws IOException {
//         response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
//         response.setHeader("Location", "/users/login.html");
//     }

//     @GetMapping("/users/register")
//     public void redirectToRegisterHtml(HttpServletResponse response) throws IOException {
//         response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
//         response.setHeader("Location", "/users/register.html");
//     }
// }
