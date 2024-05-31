package com.example.userservice.security.config;

import com.example.userservice.security.jwt.CustomLogoutFilter;
import com.example.userservice.security.jwt.JWTFilter;
import com.example.userservice.security.jwt.JWTUtil;
import com.example.userservice.security.jwt.LoginFilter;
import com.example.userservice.security.repository.RefreshRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RefreshRepository refreshRepository) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    //비밀번호를 hash로 암호화시켜서 관리
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        //session 방식에서는 session이 고정되기 때문에 csrf공격을 방어해야함.
        //jwt방식은 session을 stateless상태로 유지하기때문에 방어필요x
        http
                .csrf((auth) -> auth.disable()); //로그인 화면뜨는거 비활성화

        //From 로그인 방식 disable
        //왜냐면 jwt방식으로 로그인할것이기때문에
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        //왜냐면 jwt방식으로 로그인할것이기때문에
        http
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        //필터에서 인증을 마친 요청에 대해서 특정 경로에 대한 인가 작업을 진행한다.
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/user/create","/user").permitAll()
                        .requestMatchers("/verify-email","/verification-code").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/reissue").permitAll()
                        .anyRequest().authenticated()); //그외 나머지 요청에 대해서는 로그인한 사용자만 접근가능

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil,refreshRepository), UsernamePasswordAuthenticationFilter.class);

        //jwt필터 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtil),LoginFilter.class);

        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);
        //세션 stateless로 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    //@ConditionalOnProperty를 통해 스프링 설정에 h2-console이 enable되어 있을때만 작동하도록 했다.
    //이제 H2 Console에 대한 요청은 시큐리티 필터를 지나지 않으므로 H2 Console을 자유롭게 이용할 수 있다.
    //또한 개발 환경이나 운영 환경에서는 spring.h2.console.enabled를 사용하지 않거나
    //false로 설정할 것이기 때문에 자연스럽게 해당 빈이 생성되지 않아 h2에 대한 흔적을 지울 수 있다.
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }
}
