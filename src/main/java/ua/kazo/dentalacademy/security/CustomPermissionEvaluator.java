package ua.kazo.dentalacademy.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import ua.kazo.dentalacademy.service.OrderService;
import ua.kazo.dentalacademy.service.PurchaseDataService;

import java.io.Serializable;

@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final PurchaseDataService purchaseDataService;
    private final OrderService orderService;

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
        return hasPrivilege(auth, null, targetType, permission);
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, targetId, targetType.toUpperCase(), permission);
    }

    private boolean hasPrivilege(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if (Permission.READ.equals(permission)) {
            if (TargetType.PROGRAM.equals(targetType)) {
                return purchaseDataService.isProgramPurchasedAndNotExpired((Long) targetId, auth.getName());
            }
            if (TargetType.FOLDER.equals(targetType)) {
                return purchaseDataService.isFolderPurchasedAndNotExpired((Long) targetId, auth.getName());
            }
            if (TargetType.ORDER.equals(targetType)) {
                return orderService.existsByIdAndUserEmail((Long) targetId, auth.getName());
            }
        }
        return false;
    }

}
