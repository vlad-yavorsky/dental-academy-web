package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.entity.PurchaseData;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.enumerated.OfferingType;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
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

    public List<Offering> findAll() {
        return offeringRepository.findAll(Sort.by("id"));
    }

    public Offering findById(Long id) {
        return offeringRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.OFFERING_NOT_FOUND, id));
    }

    public Offering findByIdFetchProgramsAndFolders(Long id) {
        Offering offering = offeringRepository.findByIdFetchPrograms(id)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.OFFERING_NOT_FOUND, id));
        offeringRepository.findByIdFetchFolders(id);
        return offering;
    }

    public List<Offering> findAllByOfferingIdsIfActiveFetchProgramsAndFolders(Long programId) {
        LocalDateTime now = LocalDateTime.now();
        List<Long> allByProgramId = offeringRepository.findAllIdsByProgramId(programId);
        List<Offering> result = offeringRepository.findAllByIdsIfActiveFetchPrograms(allByProgramId, now);
        offeringRepository.findAllByIdsIfActiveFetchFolders(allByProgramId, now);
        return result;
    }

    public List<Offering> findAllByProgramId(Long programId) {
        return offeringRepository.findAllByProgramId(programId);
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

    public void buy(Long id, String userEmail) {
        LocalDateTime now = LocalDateTime.now();
        Offering offering = offeringRepository.findByIdIfAvailableForPurchase(id, now)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.OFFERING_NOT_FOUND, id));
        User user = userService.findByEmail(userEmail);
        purchaseDataService.create(PurchaseData.of(offering, user, now, now.plusMonths(offering.getTerm()), offering.getPrice()));
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
