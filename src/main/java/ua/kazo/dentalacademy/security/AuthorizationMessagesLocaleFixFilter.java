package ua.kazo.dentalacademy.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Fix locale for Spring Security messages.
 * For example, when user logged unsuccessfully, then error message is shown in default browser locale, because
 * locale is resolving in DispatcherServlet class, when message is shown before that code is executed
 */
@Component
@RequiredArgsConstructor
public class AuthorizationMessagesLocaleFixFilter extends OncePerRequestFilter {

    private final LocaleResolver localeResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        LocaleContext localeContext = buildLocaleContext(request);
        if (localeContext != null) {
            LocaleContextHolder.setLocaleContext(localeContext);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * @see DispatcherServlet#buildLocaleContext(HttpServletRequest request)
     */
    protected LocaleContext buildLocaleContext(final HttpServletRequest request) {
        LocaleResolver lr = this.localeResolver;
        if (lr instanceof LocaleContextResolver) {
            return ((LocaleContextResolver) lr).resolveLocaleContext(request);
        } else {
            return () -> (lr != null ? lr.resolveLocale(request) : request.getLocale());
        }
    }

}
