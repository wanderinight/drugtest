package com.example.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll() // 允许所有请求
        )
        .csrf().disable(); // 禁用CSRF保护
    return http.build();
}

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable()) // 禁用CSRF（开发环境适用）
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers(
    //                 "/api/auth/**",
    //                 "/actuator/**",
    //                 "/v3/api-docs/**",
    //                 "/swagger-ui/**"
    //             ).permitAll() // 放行认证接口和监控端点
    //             .anyRequest().authenticated() // 其他请求需要认证
    //         )
    //         .formLogin(form -> form.disable()) // 禁用表单登录
    //         .httpBasic(basic -> basic.disable()); // 禁用HTTP Basic认证

    //     return http.build();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 使用 BCrypt 加密算法
    }
}

