package com.example.rolehierarchysample.global;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("role : {}",authentication.getAuthorities());

        String path = "/test";

        if (!authentication.getAuthorities().isEmpty()) {

            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String authStr = authority.getAuthority();
                Role role = Role.valueOfKey(String.valueOf(authStr));
                path = path + "/" + role.getSubKey().toLowerCase();

                break;
            }

        }

        response.sendRedirect(path);
    }
}
