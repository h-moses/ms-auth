package com.ms.common.utils;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JwtUtil {

    private static final long tokenExpiration = 365 * 24 * 60 * 60 * 1000;

    private static final String tokenSignKey = "123456";


    public static String createToken(String userId, String userName) {
        String s = Jwts.builder()
                .setSubject("AUTH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return s;
    }

    //从token字符串获取userid
    public static String getUserId(String token) {
        try {
            if (!StringUtils.hasText(token)) return null;

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            String userId = (String) claims.get("userId");
            return userId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //从token字符串获取username
    public static String getUsername(String token) {
        try {
            if (!StringUtils.hasText(token)) return "";

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("userName");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
