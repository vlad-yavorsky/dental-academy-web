package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.enumerated.LiqPayPaymentStatus;
import ua.kazo.dentalacademy.repository.PurchaseDataRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseDataService {

    private final PurchaseDataRepository purchaseDataRepository;

    public boolean isProgramPurchasedAndNotExpired(Long programId, String email) {
        return purchaseDataRepository.isProgramPurchasedAndNotExpired(programId, LocalDateTime.now(), email, LiqPayPaymentStatus.SUCCESS);
    }

    public boolean isFolderPurchasedAndNotExpired(Long folderId, String email) {
        return purchaseDataRepository.isFolderPurchasedAndNotExpired(folderId, LocalDateTime.now(), email, LiqPayPaymentStatus.SUCCESS);
    }

}
