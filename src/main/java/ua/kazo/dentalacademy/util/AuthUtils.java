package ua.kazo.dentalacademy.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.kazo.dentalacademy.entity.User;

import java.util.Collection;

@UtilityClass
public class AuthUtils {

    public void updateAuthenticationAfterCredentialsChange(final User user) {
        Collection<? extends GrantedAuthority> currentAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), currentAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
