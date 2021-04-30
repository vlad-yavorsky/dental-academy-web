package ua.kazo.dentalacademy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import ua.kazo.dentalacademy.enumerated.Role;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // needed for register page, when website opened first time on it
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority(Role.ADMIN.getAuthority())
                .antMatchers("/webjars/**", "/css/**", "/js/**", "/vendor/**", "/favicon.ico", "/api/payment/**", "/activate/**").permitAll()
                .antMatchers("/login", "/register").anonymous()
                .antMatchers("/**").authenticated()
                .and().formLogin().loginPage("/login")
                .and().logout()
                .and().csrf().ignoringAntMatchers("/api/**");
    }

}
