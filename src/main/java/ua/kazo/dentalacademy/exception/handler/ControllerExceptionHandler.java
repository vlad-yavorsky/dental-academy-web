package ua.kazo.dentalacademy.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.kazo.dentalacademy.exception.ApplicationException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final String ERROR_PAGE = "/error";

    @ModelAttribute("request")
    public HttpServletRequest getRequest(HttpServletRequest request) {
        return request;
    }

    @ModelAttribute("response")
    public HttpServletResponse getRequest(HttpServletResponse response) {
        return response;
    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView exceptionHandler(final Exception e, final HttpServletRequest request,
                                         final HttpServletResponse response, final RedirectAttributes redirectAttributes) {
        log.error("Exception", e);
        ModelAndView modelAndView = new ModelAndView();
        if (e instanceof ApplicationException) {
            modelAndView.setStatus(((ApplicationException) e).getExceptionCode().getStatus());
        } else if (e instanceof AccessDeniedException) {
            modelAndView.setStatus(HttpStatus.FORBIDDEN);
        } else {
            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        modelAndView.setViewName(ERROR_PAGE);
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

}
