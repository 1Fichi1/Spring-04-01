package com.example.soratech.config;

import com.example.soratech.security.CustomUserDetailsService;
import com.example.soratech.security.JwtAuthenticationEntryPoint;
import com.example.soratech.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Swagger UI и API Documentation (ВАЖНО: должны быть первыми!)
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/v3/api-docs/**", "/api-docs/**").permitAll()
                .requestMatchers("/swagger-resources/**", "/webjars/**").permitAll()
                // REST API - Аутентификация (публичный доступ)
                .requestMatchers("/api/v1/auth/**").permitAll()
                // Статические ресурсы
                .requestMatchers("/icons/**", "/images/**", "/css/**", "/js/**").permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/favicon.ico").permitAll()
                // Публичные страницы
                .requestMatchers("/", "/index", "/register", "/forgot-password").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                // REST API - Доступ по ролям
                .requestMatchers("/api/v1/admin/**").hasRole("Администратор")
                .requestMatchers("/api/v1/manager/**").hasAnyRole("Администратор", "Менеджер")
                .requestMatchers("/api/v1/client/**").hasAnyRole("Администратор", "Менеджер", "Клиент")
                // Web панели - Доступ по ролям
                .requestMatchers("/admin/**", "/api/admin/**").hasRole("Администратор")
                .requestMatchers("/manager/**", "/api/manager/**").hasAnyRole("Администратор", "Менеджер")
                .requestMatchers("/client/**", "/api/client/**").hasAnyRole("Администратор", "Менеджер", "Клиент")
                // Профиль доступен всем авторизованным
                .requestMatchers("/profile", "/profile/**").authenticated()
                // Остальные требуют авторизации
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginProcessingUrl("/perform_login")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1) // Одна сессия на пользователя
                .maxSessionsPreventsLogin(false) // Вытеснять старую сессию
            )
            .rememberMe(remember -> remember
                .key("uniqueAndSecret")
                .tokenValiditySeconds(86400 * 7) // 7 дней
            )
            .csrf(csrf -> csrf.disable()) // Отключаем CSRF для простоты (в продакшене не рекомендуется)
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // После авторизации всегда перенаправляем на профиль
            response.sendRedirect("/profile");
        };
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
