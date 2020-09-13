package ua.kazo.dentalacademy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ua.kazo.dentalacademy.enumerated.Role;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority(Role.ADMIN.getAuthority())
                .antMatchers("/webjars/**", "/css/**", "/js/**", "/vendor/**", "/api/payment/**").permitAll()
                .antMatchers("/login", "/register").anonymous()
                .antMatchers("/**").authenticated()
                .and().formLogin().loginPage("/login")
                .and().logout()
                .and().csrf().ignoringAntMatchers("/api/**");
    }

}
