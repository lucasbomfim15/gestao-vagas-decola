package com.example.demo.security;

import com.example.demo.providers.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityCompanyFilter extends OncePerRequestFilter {

    @Autowired
    JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

//        SecurityContextHolder.getContext().setAuthentication(null);
        String header = request.getHeader("Authorization");

        if(request.getRequestURI().startsWith("/company")){
            if(header != null) {
                var decodedJWT = this.jwtProvider.validateToken(header);
                if(decodedJWT == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // Extrai APENAS o subject (company_id) do token
                String companyId = decodedJWT.getSubject();

                // Armazena apenas a String do company_id
                request.setAttribute("company_id", companyId);

                var roles = decodedJWT.getClaim("roles").asList(Object.class);
                var grants = roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString())).toList();

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        decodedJWT.getSubject(), null, grants);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }



        filterChain.doFilter(request, response);
    }
}