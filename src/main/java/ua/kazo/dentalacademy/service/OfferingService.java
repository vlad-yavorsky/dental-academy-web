package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.entity.PurchaseData;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.enumerated.OfferingType;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.OfferingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferingService {

    private final UserService userService;
    private final OfferingRepository offeringRepository;
    private final PurchaseDataService purchaseDataService;
    private final MessageSource messageSource;

    public List<Offering> findAll() {
        return offeringRepository.findAll(Sort.by("id"));
    }

    public Offering findById(Long id) {
        return offeringRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.OFFERING_NOT_FOUND, id));
    }

    public Offering findByIdFetchProgramsAndFolders(Long id) {
        Offering offering = offeringRepository.findByIdFetchPrograms(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.OFFERING_NOT_FOUND, id));
        offeringRepository.findByIdFetchFolders(id);
        return offering;
    }

    public List<Offering> findAllByOfferingIdsIfActiveFetchProgramsAndFolders(List<Long> offeringIds) {
        LocalDateTime now = LocalDateTime.now();
        List<Offering> result = offeringRepository.findAllByIdsIfActiveFetchPrograms(offeringIds, now);
        offeringRepository.findAllByIdsIfActiveFetchFolders(offeringIds, now);
        return result;
    }

    public List<Offering> findAllByProgramId(Long programId) {
        return offeringRepository.findAllByProgramId(programId);
    }

    public List<Long> findAllIdsByProgramId(Long programId) {
        return offeringRepository.findAllIdsByProgramId(programId);
    }

    public boolean existsByNameAndType(String name, OfferingType type) {
        return offeringRepository.existsByNameAndType(name, type);
    }

    public boolean existsByNameAndTypeAndIdNot(String name, OfferingType type, Long id) {
        return offeringRepository.existsByNameAndTypeAndIdNot(name, type, id);
    }

    public Offering save(Offering offering) {
        return offeringRepository.save(offering);
    }

    public void buy(Long id, String email) {
        if (purchaseDataService.existsByIdOfferingIdAndUserEmail(id, email)) {
            throw new ApplicationException(messageSource, ExceptionCode.OFFERING_ALREADY_PURCHASED, id, email);
        }
        LocalDateTime now = LocalDateTime.now();
        Offering offering = offeringRepository.findByIdIfAvailableForPurchase(id, now)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.OFFERING_NOT_FOUND, id));
        User user = userService.findByEmail(email);
        purchaseDataService.create(PurchaseData.of(offering, user, now));
    }

    public void activateOffering(Long id) {
        findById(id).setDeactivated(null);
    }

    public void deactivateOffering(Long id) {
        findById(id).setDeactivated(LocalDateTime.now());
    }

    public void delete(Long id) {
        offeringRepository.deleteById(id);
    }

}
