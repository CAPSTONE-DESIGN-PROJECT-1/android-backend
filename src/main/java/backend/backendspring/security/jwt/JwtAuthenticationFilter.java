package backend.backendspring.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. Request Header 에서 JWT 토큰 추출
        String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        // 2. validateToken 으로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateAccessToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
            // 1. 토큰이 유효하면 토큰으로부터 유저 정보 받아옴
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // 2. SecurityContext 에 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

}
