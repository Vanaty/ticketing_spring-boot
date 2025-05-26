package mg.itu.ticketing.config;

import mg.itu.ticketing.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                .requestMatchers("/reservations/api/*/pdf").permitAll() // Permettre l'accès à l'API PDF sans authentification
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/pricing", true)
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            )
            .userDetailsService(customUserDetailsService);
        
        // Désactiver la protection CSRF pour les endpoints d'API
        http.csrf(csrf -> csrf
            .ignoringRequestMatchers("/reservations/api/**")
        );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }
}
