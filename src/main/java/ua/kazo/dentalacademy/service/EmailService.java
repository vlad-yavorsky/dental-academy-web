package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.properties.AppProperties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
@RequiredArgsConstructor
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
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");
        helper.setSubject(createCommonMailSubject("mail.account.activation.title", locale));
        helper.setTo(user.getEmail());
        helper.setText(htmlContent, true);
        javaMailSender.send(msg);
    }

    private String createCommonMailSubject(String mailTitleCode, Locale locale) {
        String sitename = messageSource.getMessage("sitename", null, locale);
        String mailTitle = messageSource.getMessage(mailTitleCode, null, locale);
        return sitename + " - " + mailTitle;
    }

}
