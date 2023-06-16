package backend.backendspring.security;

import backend.backendspring.security.jwt.JwtAuthenticationFilter;
import backend.backendspring.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
// 자동으로 생성자 및 생성자 주입 만듭니다.
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호 암호화 (Bcrypt 방식 ??)
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // 일반적인 루트가 아닌 다른 방식으로 요청시 거절, header 에 id, pw가 아닌 token(jwt)을 달고 감, 그래서 basic 이 아닌 bearer 를 사용합니다.
                .csrf().disable() // 스프링 시큐리티의 CSRF 보호 기능을 비활성화합니다. (좋은 방법은 아닌듯..)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용 안하는걸로 설정합니다.
                .and()
                .authorizeRequests() // 요청에 대한 사용 권한 체크합니다.
                .antMatchers("/users/{userIdx}/d").authenticated() // 인증된 사용자의 접근을 허용합니다.
                .antMatchers("/parties/**").authenticated()
                .anyRequest().permitAll() // 그 외 나머지 요청은 누구나 접근 가능합니다.
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter 를 UsernamePasswordAuthenticationFilter 전에 넣음


        return http.build();
    }

}
