package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.entity.Event;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.properties.AppProperties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final AppProperties appProperties;
    private final MessageSource messageSource;
    private final TemplateEngineDecorator templateEngineDecorator;

    public void sendUserActivationLink(User user, Locale locale) throws MessagingException {
        Context ctx = new Context(locale);
        ctx.setVariable("user", user);
        ctx.setVariable("host", appProperties.getHost());
        String htmlContent = templateEngineDecorator.findAndProcessLocalizedTemplate("mail/activation", ctx);
        MimeMessage msg = createMessage("mail.account.activation.title", locale, user.getEmail(), htmlContent);
        javaMailSender.send(msg);
    }

    public void sendUserRegisteredForEvent(User user, Event event) {
        Locale locale = AppConfig.Constants.DEFAULT_LOCALE;
        Context ctx = new Context(locale);
        ctx.setVariable("user", user);
        ctx.setVariable("event", event);
        ctx.setVariable("host", appProperties.getHost());
        String htmlContent = templateEngineDecorator.process("mail/user-registered-for-event_uk", ctx);
        MimeMessage msg;
        try {
            msg = createMessage("mail.event.user.registration.title", locale, appProperties.getNotificationEmails(), htmlContent);
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }

    private MimeMessage createMessage(String subjectCode, Locale locale, String to, String htmlContent) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");
        helper.setSubject(createCommonMailSubject(subjectCode, locale));
        helper.setTo(to);
        helper.setText(htmlContent, true);
        return msg;
    }

    private String createCommonMailSubject(String mailTitleCode, Locale locale) {
        String sitename = messageSource.getMessage("sitename", null, locale);
        String mailTitle = messageSource.getMessage(mailTitleCode, null, locale);
        return sitename + " - " + mailTitle;
    }

}
