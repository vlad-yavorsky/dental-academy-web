package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.enumerated.LiqPayPaymentStatus;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.ProgramRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgramService {

    private final ProgramRepository programRepository;
    private final MessageSource messageSource;

    public Page<Program> findAll(Pageable pageable) {
        return programRepository.findAll(pageable);
    }

    public List<Program> findAll() {
        return programRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public List<Program> findAllWithFolders() {
        return programRepository.findAllWithFolders();
    }

    public Page<Program> findAllByNotDeactivatedOfferings(String search, Pageable pageable) {
        if (!StringUtils.isEmpty(search)) {
            return programRepository.findAllByNotDeactivatedOfferingsAndName(LocalDateTime.now(), search, pageable);
        }
        return programRepository.findAllByNotDeactivatedOfferings(LocalDateTime.now(), pageable);
    }

    public Program findById(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.PROGRAM_NOT_FOUND, id));
    }

    public Program findByIdAndFolderCategoryFetchFolders(Long id, FolderCategory category) {
        return programRepository.findByIdAndFolderCategoryFetchFolders(id, category)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.PROGRAM_NOT_FOUND, id));
    }

    public boolean existsByIdAndFolderCategory(Long id, FolderCategory category) {
        return programRepository.existByIdAndFolderCategory(id, category);
    }

    public boolean existsByName(String name) {
        return programRepository.existsByName(name);
    }

    public boolean existsByNameAndIdNot(String name, Long id) {
        return programRepository.existsByNameAndIdNot(name, id);
    }

    public Program save(Program program) {
        return programRepository.save(program);
    }

    public Page<Program> findAllByNotExpiredPurchase(String email, String search, Pageable pageable) {
        if (!StringUtils.isEmpty(search)) {
            return programRepository.findAllByNotExpiredPurchaseAndName(email, LocalDateTime.now(), search, pageable, LiqPayPaymentStatus.SUCCESS);
        }
        return programRepository.findAllByNotExpiredPurchase(email, LocalDateTime.now(), pageable, LiqPayPaymentStatus.SUCCESS);
    }

    public void delete(Long id) {
        programRepository.deleteById(id);
    }

}
