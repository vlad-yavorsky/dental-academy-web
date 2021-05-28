package ua.kazo.dentalacademy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.LocaleResolver;
import ua.kazo.dentalacademy.enumerated.Role;
import ua.kazo.dentalacademy.security.AuthorizationMessagesLocaleFixFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // needed for register page, when website opened first time on it
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority(Role.ADMIN.getAuthority())
                .antMatchers("/webjars/**", "/css/**", "/js/**", "/vendor/**", "/favicon.ico",
                        "/api/payment/**", "/activate/**", "/article/**").permitAll()
                .antMatchers("/login", "/register").anonymous()
                .antMatchers("/**").authenticated()
                .and().formLogin().loginPage("/login")
                .and().logout()
                .and().csrf().ignoringAntMatchers("/api/**")
                .and().addFilterBefore(new AuthorizationMessagesLocaleFixFilter(localeResolver), UsernamePasswordAuthenticationFilter.class);
    }

}
