package com.example.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

// JWT工具类
@Component
public class JwtTokenUtil {
    
    // 安全密钥，实际项目中应从配置中读取
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Token有效期（毫秒），这里设置为10小时
    private static final long EXPIRATION_TIME = 36000000; 
    
// 生成Token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // 可以添加额外信息，如用户角色权限
        return doGenerateToken(claims, userDetails.getUsername());
    }
    // 带额外信息的Token生成
    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))// 设置过期时间
                .signWith(SECRET_KEY)
                .compact();
    }

    // 验证Token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 从Token中提取用户名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 检查Token是否过期
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 提取过期时间
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 从Token中提取信息
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // 从Token中提取所有声明
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }







    /**
     * 生成JWT Token
     * @param staffid 员工ID
     * @return 生成的Token字符串
     */
    // public static String generateToken(String staffid) {
    //     return Jwts.builder()
    //             .setSubject(staffid) // 设置主题（通常放用户唯一标识）
    //             .setIssuedAt(new Date()) // 设置签发时间
    //             .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 设置过期时间
    //             .signWith(SECRET_KEY) // 使用密钥签名
    //             .compact(); // 生成字符串
    // }
    
    // /**
    //  * 验证并解析Token
    //  * @param token JWT Token
    //  * @return 员工ID
    //  */
    // public static String validateTokenAndGetEmployeeId(String token) {
    //     return Jwts.parserBuilder()
    //             .setSigningKey(SECRET_KEY)
    //             .build()
    //             .parseClaimsJws(token)
    //             .getBody()
    //             .getSubject();
    // }
}




