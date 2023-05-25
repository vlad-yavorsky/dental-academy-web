package ua.kazo.dentalacademy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.LocaleResolver;
import ua.kazo.dentalacademy.enumerated.Role;
import ua.kazo.dentalacademy.security.AuthorizationMessagesLocaleFixFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final LocaleResolver localeResolver;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        provider.setMessageSource(messageSource);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // needed for register page, when website opened first time on it
                .and()
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin/**").hasAuthority(Role.ADMIN.getAuthority())
                        .requestMatchers("/webjars/**", "/css/**", "/js/**", "/vendor/**", "/favicon.ico",
                                "/api/payment/**", "/activate/**", "/article/**").permitAll()
                        .requestMatchers("/login", "/register").anonymous()
                        .requestMatchers("/**").authenticated()
                )
                .formLogin().loginPage("/login")
                .and().logout()
                .and().csrf().ignoringRequestMatchers("/api/**")
                .and().addFilterBefore(new AuthorizationMessagesLocaleFixFilter(localeResolver), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
