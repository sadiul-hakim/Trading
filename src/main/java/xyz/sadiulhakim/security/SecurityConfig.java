package xyz.sadiulhakim.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain config(HttpSecurity http) throws Exception {

        String[] permitted = {
                "/css/**",
                "/js/**",
                "/image/**",
                "/font/**",
                "/register_page",
                "/register"
        };

        String[] adminAccess = {
                "/stock/create_page",
                "/stock/create",
                "/user/**"
        };

        return http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth.requestMatchers(permitted).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(adminAccess).hasRole("ADMIN"))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .userDetailsService(userDetailsService)
                .formLogin(login -> login.loginPage("/login_page")
                        .loginProcessingUrl("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true)
                        .failureForwardUrl("/login_page?error=true")
                )
                .logout(logout -> logout.logoutUrl("/logout")
                        .permitAll()
                        .logoutSuccessUrl("/login_page?logout=true")
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true))
                .build();
    }
}
