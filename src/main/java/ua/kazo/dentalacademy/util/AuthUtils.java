package ua.kazo.dentalacademy.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.kazo.dentalacademy.entity.User;

import java.security.Principal;
import java.util.Collection;

public final class AuthUtils {

    private AuthUtils() {}

    public static void updateAuthenticationAfterCredentialsChange(final User user) {
        Collection<? extends GrantedAuthority> currentAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), currentAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void updateCartItemsCount(final Principal principal, int counter) {
        ((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).setCartItemsCount(counter);
    }

    public static User getUser(final Principal principal) {
         return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }

}
