package com.book.gpt.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Lazy
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService; // CustomUserDetailsService를 주입합니다.
    @Autowired
    private ApplicationContext applicationContext;
    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return bCryptPasswordEncoder();
    }
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter();
        jwtAuthorizationFilter.setAuthenticationManager(authenticationManagerBean()); // AuthenticationManager를 주입
        return jwtAuthorizationFilter;
    }

    @Bean(name = "customUserDetailsService")
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsServiceImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthorizationFilter jwtAuthorizationFilter = applicationContext.getBean(JwtAuthorizationFilter.class);
        http.cors().and().csrf().disable()
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class) // JwtAuthorizationFilter를 UsernamePasswordAuthenticationFilter 전에 추가
                .authorizeRequests()
                .antMatchers("/public/**").permitAll() // 모든 사용자에게 접근 허용
                .antMatchers("/users/login").permitAll() // 로그인 엔드포인트 허용
                .antMatchers("/users/check-login").permitAll() // 로그인 엔드포인트 허용
                .antMatchers("/users/signup").permitAll() // 로그인 엔드포인트 허용
                .antMatchers("/api//send-email").permitAll() // 이메일 엔드포인트 허용
                .antMatchers("/api//verify-email").permitAll() // 이메일 엔드포인트 허용
                .antMatchers("/users/**").hasRole("USER") // USER 권한을 가진 사용자만 접근 허용
                .antMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한을 가진 사용자만 접근 허용
                .anyRequest().authenticated(); // 다른 모든 요청은 인증이 필요
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT"));
        config.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


}