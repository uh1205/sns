package com.sparta.sns.util;

import com.sparta.sns.user.entity.UserRole;
import com.sparta.sns.user.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "RefreshToken";
    public static final String AUTHORIZATION_KEY = "auth";

    private static final int ACCESS_TOKEN_TIME = 10 * 1000;
    private static final int REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000;

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(String username, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(String username, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username) // 갱신용 토큰이기 때문에 사용자 정보는 굳이 안 넣어도 된다?
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Cookie에 Refresh 토큰 저장
     */
    public void saveTokenToCookie(String token, HttpServletResponse response) {
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, encodedToken);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(REFRESH_TOKEN_TIME);

        response.addCookie(cookie);
    }

    /**
     * Header에서 Access 토큰 가져오기
     */
    public String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    /**
     * Cookie에서 Refresh 토큰 가져오기
     */
    public String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8)
                        .substring(7);
            }
        }
        return null;
    }

    /**
     * Access 토큰 검증
     */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    /**
     * Refresh 토큰 검증
     */
    public boolean validateRefreshToken(String token) {
        log.info("Refresh 토큰 검증");
        String username = getUsernameFromToken(token);
        return refreshTokenRepository.findByUsername(username).isPresent();
    }

    /**
     * 토큰에서 username 가져오기
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 토큰에서 role 가져오기
     */
    public UserRole getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        String role = claims.get(AUTHORIZATION_KEY, String.class);
        return UserRole.valueOf(role);
    }

}
