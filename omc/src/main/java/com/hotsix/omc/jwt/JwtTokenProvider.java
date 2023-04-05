package com.hotsix.omc.jwt;


import com.hotsix.omc.domain.form.token.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final long TOKEN_EXPIRE_TIME;
    private final static String AUTH = "auth";
    private final static String BEARER_TYPE = "Bearer";


    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.token-expire-time}") long accessTime){

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.TOKEN_EXPIRE_TIME = accessTime;
    }

    // 유저 정보로 권한 토큰 생성
    public TokenInfo generateToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = Instant.now().toEpochMilli();

        // 토큰 생성, 유효기간 1일
        Date tokenExpireIn = new Date(now + TOKEN_EXPIRE_TIME);
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTH, authorities)
                .setExpiration(tokenExpireIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(token)
                .build();
    }

    // 토큰 복호화 메서드
    public Authentication getAuthentication(String token){
        Claims claims = parseClaims(token);

        if (claims.get(AUTH) == null){
            log.error("NO AUTHORITIES");
            throw new JwtException("토큰 권한 정보가 없습니다.");
        }

        //payloads 권한정보
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTH).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    // 토큰 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.error("Invalid JWT signature.", e);
            throw new JwtException("잘못된 시그니처");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT", e);
            throw new JwtException("유효하지 않은 JWT");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT", e);
            throw new JwtException("만료");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT", e);
            throw new JwtException("지원하지않는 JWT");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
            throw new JwtException("JWT claims 이 빈칸");
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
