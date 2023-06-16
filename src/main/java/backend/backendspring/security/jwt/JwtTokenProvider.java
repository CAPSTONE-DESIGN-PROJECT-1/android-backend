package backend.backendspring.security.jwt;

import backend.backendspring.security.SecurityUserDetailService;
import backend.backendspring.src.user.model.entity.Role;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

// 토큰을 생성하고 검증하는 클래스입니다.
// 해당 컴포넌트는 필터클래스에서 사전 검증을 거칩니다.
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    // Access 토큰 유효시간 30분
    private long accessTokenValidTime = 48 * 30 * 60 * 1000L;

    private final SecurityUserDetailService securityUserDetailService;

    // 객체 초기화, secretKey를 Base64로 인코딩
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createAccessToken(Long userIdx, Role roles) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userIdx)); // JWT payload에 저장되는 정보 단위
        claims.put("roles", roles); // 정보 저장 (key-value)
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature에 들어갈 secret 값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = securityUserDetailService.loadUserByUsername(this.getUserPK(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPK(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옵니다. "ACCESS_TOKEN": "TOKEN 값"
    public String resolveAccessToken(HttpServletRequest request) {
        return request.getHeader("ACCESS_TOKEN");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateAccessToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        }
        return false;
    }

}
