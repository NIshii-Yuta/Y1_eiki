package com.college.yi.bookmaneger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.college.yi.bookmaneger.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    public SecurityConfig(CustomUserDetailsService uds) {
        this.userDetailsService = uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          // ここで認証プロバイダを直接セット
          .authenticationProvider(authProvider())

          // 認可ルール
          .authorizeHttpRequests(authz -> authz
              .requestMatchers(HttpMethod.GET,    "/api/books").hasAnyRole("USER","ADMIN")
              .requestMatchers(HttpMethod.POST,   "/api/books").hasRole("ADMIN")
              .requestMatchers(HttpMethod.PUT,    "/api/books/**").hasRole("ADMIN")
              .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")
              .anyRequest().permitAll()
          )
          .exceptionHandling(e -> e
                  .defaultAuthenticationEntryPointFor(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                    new AntPathRequestMatcher("/api/**")
                  )
                )

          // デフォルトログインフォーム
          .formLogin(form -> form
              .permitAll()
          )
          .logout(logout -> logout
              .permitAll()
          );

        // CSRF はデフォルトで有効。Fetch の際にヘッダか Cookie で送ってください。
        return http.build();
    }
}
