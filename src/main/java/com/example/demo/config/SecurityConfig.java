package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.utils.JwtAuthenticationFilter;

@Configuration// 标记为Spring配置类
@EnableWebSecurity//启用Spring Security
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 构造函数注入Jwt过滤器（使用@Lazy解决循环依赖）
    public SecurityConfig(@Lazy JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    // 配置安全过滤链，配置哪些请求需要认证，哪些可以公开访问
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 禁用CSRF，因为使用JWT
            .cors(cors -> cors.disable()) // 禁用CORS，或根据需要配置(由前端处理或Nginx配置)

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 使用无状态会话

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // 认证相关端点放行
                .requestMatchers(
                    "/swagger-ui/**", 
                    "/v3/api-docs/**",
                    "/swagger-resources/**"
                    ).permitAll() // Swagger放行
                .anyRequest().authenticated() // 其他请求需要认证
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 添加JWT过滤器

        return http.build();
    }
    //认证管理器，用于处理用户认证,暴露AuthenticationManager到Spring容器
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    //密码编码器，用于密码加密和验证
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}