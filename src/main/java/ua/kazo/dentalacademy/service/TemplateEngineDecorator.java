package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateEngineDecorator {

    private final TemplateEngine templateEngine;
    private final List<String> availableLanguages = List.of("uk", "ru");

    public final String findAndProcessLocalizedTemplate(final String template, final IContext context) {
        String localizedTemplate = template;
        if (availableLanguages.contains(context.getLocale().getLanguage())) {
            localizedTemplate = template + "_" + context.getLocale().getLanguage();
        }
        return templateEngine.process(localizedTemplate, context);
    }

}
