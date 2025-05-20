//package com.lms.resource_service.integartion_tests;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.util.List;
//
//public class TestSecurityUtil {
//    public static void setAdminAuthentication() {
//        var authentication = new UsernamePasswordAuthenticationToken(
//                "admin@gmail.com",
//                null,
//                List.of(new SimpleGrantedAuthority("ADMIN"))
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    public static void clearAuthentication() {
//        SecurityContextHolder.clearContext();
//    }
//}
