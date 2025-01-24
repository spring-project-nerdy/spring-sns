package com.fastcampus.sns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

  public static String generateToken(String userName, String key, long expiredTimeMs) {
    Claims claims = Jwts.claims();
    claims.put("username", userName);
  
    return Jwts.builder()
        .setClaims(claims) // Claims 설정
        .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간
        .setExpiration(new Date(System.currentTimeMillis() * expiredTimeMs)) // 만료 시간
        .setSubject(userName)  // username을 subject로 설정
        .signWith(getKey(key), SignatureAlgorithm.HS256) // 서명
        .compact(); // 토큰 생성
  }
  
  // 키를 생성하는 메서드
  private static Key getKey(String key) {
    byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes); // HMAC SHA 알고리즘을 위한 키 생성
  }
}
