package com.example.demo.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

// JWT工具类
public class JwtTokenUtil {
    
    // 安全密钥，实际项目中应从配置中读取
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Token有效期（毫秒），这里设置为2小时
    private static final long EXPIRATION_TIME = 7200000; 
    
    /**
     * 生成JWT Token
     * @param staffid 员工ID
     * @return 生成的Token字符串
     */
    public static String generateToken(String staffid) {
        return Jwts.builder()
                .setSubject(staffid) // 设置主题（通常放用户唯一标识）
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 设置过期时间
                .signWith(SECRET_KEY) // 使用密钥签名
                .compact(); // 生成字符串
    }
    
    /**
     * 验证并解析Token
     * @param token JWT Token
     * @return 员工ID
     */
    public static String validateTokenAndGetEmployeeId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}




