package com.book.gpt.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtAuthorizationFilter extends UsernamePasswordAuthenticationFilter {
    @Value("${jwt.secret}") // 시크릿 키는 설정에서 가져옵니다.
    private String secretKey;


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request); // 요청에서 토큰 추출
            if (token != null) {
                // 토큰을 검증하고 사용자 정보를 추출
                UserDetails userDetails = validateToken(token);

                JwtAuthentication jwtAuthentication = new JwtAuthentication(userDetails.getUsername());
                Authentication authentication = new UsernamePasswordAuthenticationToken(jwtAuthentication, null, jwtAuthentication.getAuthorities());
                // 인증 객체를 SecurityContext에 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // 예외 처리
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        // 다음 필터로 이동
        chain.doFilter(request, response);
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 86400000); // 토큰 만료 시간 (예: 24시간)

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        // 요청에서 토큰 추출 (예: Authorization 헤더에서 Bearer 토큰)
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private UserDetails validateToken(String token) {
        // 토큰 검증 및 사용자 정보 추출
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        Claims body = claimsJws.getBody();
        String username = body.getSubject();

        return new JwtAuthentication(username);
    }
}
