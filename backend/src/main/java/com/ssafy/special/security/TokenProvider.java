package com.ssafy.special.security;

import com.learnershigh.dto.user.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${jwt.secret.key}")
    private String salt;

    private Key secretKey;

    // 만료시간 : 15분
    private final long accessTokenValidTime = 1000L * 60 * 15;
    // 만료시간 : 5시간
    private final long refreshTokenValidTime = 1000L * 60 * 60 * 5;

    private final CustomUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String createAccessToken(String userEmail) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() +  accessTokenValidTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 생성
    public String createRefreshToken(String userEmail) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 권한정보 획득
    // Spring Security 인증과정에서 권한확인을 위한 기능
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccount(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에 담겨있는 유저 account 획득
    public String getAccount(String token) {
        // 만료된 토큰에 대해 parseClaimsJws를 수행하면 io.jsonwebtoken.ExpiredJwtException이 발생한다.
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return e.getClaims().getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Authorization Header를 통해 인증을 한다.
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // Bearer 검증
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public TokenDto validateRefreshToken(String refreshToken) {
        try {
            // 검증
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            //refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access, refresh 토큰을 생성
            if (!claims.getBody().getExpiration().before(new Date())) {
                TokenDto token = new TokenDto();
                token.setAccessToken(createAccessToken(claims.getBody().get("sub").toString()));
                token.setRefreshToken(createRefreshToken(claims.getBody().get("sub").toString()));
                return token;
            }
        } catch (Exception e) {
            //refresh 토큰이 만료되었을 경우, 로그인 필요
            return null;
        }
        return null;
    }
}