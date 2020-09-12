package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.enumerated.OfferingType;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.OfferingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferingService {

    private final OfferingRepository offeringRepository;
    private final MessageSource messageSource;

    public Page<Offering> findAll(Pageable pageable) {
        return offeringRepository.findAll(pageable);
    }

    public Offering findById(Long id) {
        return offeringRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.OFFERING_NOT_FOUND, id));
    }

    public List<Offering> findAllById(Set<Long> ids) {
        return offeringRepository.findAllById(ids);
    }

    public Offering findByIdFetchProgramsAndFolders(Long id) {
        Offering offering = offeringRepository.findFetchProgramsById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.OFFERING_NOT_FOUND, id));
        offeringRepository.findFetchFoldersById(id);
        return offering;
    }

    public List<Offering> findAllByIdsAndNotDeactivatedFetchProgramsAndFolders(List<Long> offeringIds) {
        LocalDateTime now = LocalDateTime.now();
        offeringRepository.findAllByIdsAndNotDeactivatedFetchPrograms(offeringIds, now);
        return offeringRepository.findAllByIdsAndNotDeactivatedFetchFolders(offeringIds, now);
    }

    public List<Offering> findAllByIdInFetchProgramsAndFolders(Set<Long> ids) {
        offeringRepository.findAllFetchProgramsByIdIn(ids);
        return offeringRepository.findAllFetchFoldersByIdIn(ids);
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
        if (offering.getActivated() == null) {
            offering.setActivated(LocalDateTime.now());
        }
        return offeringRepository.save(offering);
    }

    public Offering findByIdAndActive(Long id) {
        return offeringRepository.findByIdAndActive(id, LocalDateTime.now())
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.OFFERING_NOT_FOUND, id));
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
