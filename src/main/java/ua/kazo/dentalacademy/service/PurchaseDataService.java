package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;
import ua.kazo.dentalacademy.repository.PurchaseDataRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseDataService {

    private final PurchaseDataRepository purchaseDataRepository;

    public boolean isProgramPurchasedAndNotExpired(Long programId, String email) {
        return purchaseDataRepository.isProgramPurchasedAndNotExpired(programId, LocalDateTime.now(), email, UnifiedPaymentStatus.SUCCESS);
    }

    public boolean isBonusPurchasedAndNotExpired(Long bonusId, String email) {
        return purchaseDataRepository.isBonusPurchasedAndNotExpired(bonusId, LocalDateTime.now(), email, UnifiedPaymentStatus.SUCCESS);
    }

}
