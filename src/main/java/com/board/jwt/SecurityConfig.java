package com.board.jwt;
import com.board.jwt.service.JwtAuthenticationFilter;
import com.board.jwt.service.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig {

//    직접만든 토큰provider를 통해 api요청시 filter역할
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

//    WebSecurityConfigurerAdapter가  deprecated됨에 따라 SecurityFilterChain을 사용하였다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return  http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
//                token테스팅을 위한ui, 회원가입api, doLogin api는 token filter에 걸리지 않도록 하였다.
                    .antMatchers("/token","/authors/new", "/doLogin")
                    .permitAll()
                    .anyRequest().authenticated()
                .and()
//                세션을 사용하지 않겠다는 선언
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                직접만든 tokenProvider를 통한 filter
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}