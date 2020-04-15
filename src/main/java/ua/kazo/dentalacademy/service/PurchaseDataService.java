package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.PurchaseData;
import ua.kazo.dentalacademy.repository.PurchaseDataRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseDataService {

    private final PurchaseDataRepository purchaseDataRepository;

    public List<PurchaseData> findPurchaseDataWithFoldersByEmail(String email) {
        List<PurchaseData> result = purchaseDataRepository.findAllByUserEmailFetchOfferingAndPrograms(email);
        purchaseDataRepository.findAllByUserEmailFetchOfferingAndFolders(email);
        return result;
    }

    public void create(PurchaseData purchaseData) {
        purchaseDataRepository.save(purchaseData);
    }

    public boolean existsByIdOfferingIdAndUserEmail(Long offeringId, String email) {
        return purchaseDataRepository.existsByIdOfferingIdAndUserEmail(offeringId, email);
    }

    public List<PurchaseData> findAllByIdOfferingIdInAndUserEmail(List<Long> offeringIds, String email) {
        return purchaseDataRepository.findAllByIdOfferingIdInAndUserEmail(offeringIds, email);
    }

    public boolean isProgramPurchasedAndNotExpired(Long programId, String email) {
        return purchaseDataRepository.isProgramPurchasedAndNotExpired(programId, LocalDateTime.now(), email);
    }

    public boolean isFolderPurchasedAndNotExpired(Long folderId, String email) {
        return purchaseDataRepository.isFolderPurchasedAndNotExpired(folderId, LocalDateTime.now(), email);
    }

}
