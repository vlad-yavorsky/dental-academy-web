package ua.kazo.dentalacademy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import ua.kazo.dentalacademy.security.CustomPermissionEvaluator;
import ua.kazo.dentalacademy.service.OrderService;
import ua.kazo.dentalacademy.service.PurchaseDataService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final PurchaseDataService purchaseDataService;
    private final OrderService orderService;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator(purchaseDataService, orderService));
        return expressionHandler;
    }

}
