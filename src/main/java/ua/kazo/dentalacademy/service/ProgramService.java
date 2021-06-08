package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.enumerated.ProgramCategory;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.ProgramRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgramService {

    private final ProgramRepository programRepository;
    private final MessageSource messageSource;
    private final FolderService folderService;

    public Page<Program> findAllByCategory(ProgramCategory category, Pageable pageable) {
        return programRepository.findAllByCategory(category, pageable);
    }

    public List<Program> findAll() {
        return programRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public List<Program> findAllByCategoryJoinFolders(ProgramCategory category) {
        return programRepository.findAllJoinFoldersByCategoryOrderByName(category);
    }

    public Page<Program> findAllByNotDeactivatedOfferings(String search, Pageable pageable) {
        return programRepository.findAllByNotDeactivatedOfferingsAndName(LocalDateTime.now(), search, pageable);
    }

    public Program findById(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.PROGRAM_NOT_FOUND, id));
    }

    public Program findByIdFetchFoldersOrderByFoldersOrdering(Long id) {
        return programRepository.findFetchFoldersByIdOrderByFoldersOrdering(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.PROGRAM_NOT_FOUND, id));
    }

    public Program findByIdFetchFoldersAndItemsAndViewedFolderItems(Long id) {
        Program program = findByIdFetchFoldersOrderByFoldersOrdering(id);
        List<Long> folderIds = program.getFolders().stream()
                .map(Folder::getId)
                .collect(Collectors.toList());
        folderIds.forEach(folderService::findByIdFetchItemsAndViewedFolderItems);
        return program;
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

    public Page<Program> findAllProgramsByNotExpiredPurchase(String email, String search, Pageable pageable) {
        return programRepository.findAllProgramsByNotExpiredPurchaseAndName(email, LocalDateTime.now(), search, pageable, UnifiedPaymentStatus.SUCCESS);
    }

    public Page<Program> findAllBonusesByNotExpiredPurchaseAndName(String email, String search, Pageable pageable) {
        return programRepository.findAllBonusesByNotExpiredPurchaseAndName(email, LocalDateTime.now(), search, pageable, UnifiedPaymentStatus.SUCCESS);
    }

    public void delete(Long id) {
        programRepository.deleteById(id);
    }

    public void resetProgramProgress(Long userId, Long programId) {
        findByIdFetchFoldersOrderByFoldersOrdering(programId)
                .getFolders().forEach(folder -> folderService.resetFolderProgress(userId, folder.getId()));
    }

}
