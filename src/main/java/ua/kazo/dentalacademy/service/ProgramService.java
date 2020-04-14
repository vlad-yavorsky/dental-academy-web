package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.repository.ProgramRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgramService {

    private final ProgramRepository programRepository;

    public List<Program> findAll() {
        return programRepository.findAll(Sort.by("id"));
    }

    public List<Program> findAllWithOfferings() {
        return programRepository.findAllWithActiveOfferings(LocalDateTime.now());
    }

    public Program findById(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.PROGRAM_NOT_FOUND, id));
    }

    public Program findByIdAndFolderCategoryFetchFolders(Long id, FolderCategory category) {
        return programRepository.findByIdAndFolderCategoryFetchFolders(id, category)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.PROGRAM_NOT_FOUND, id));
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

    public boolean accessToProgram(Long programId, String email) {
        return programRepository.isProgramPurchasedAndNotExpired(programId, email, LocalDateTime.now());
    }

    public boolean accessToFolder(Long folderId, String email) {
        return programRepository.isFolderPurchasedAndNotExpired(folderId, email, LocalDateTime.now());
    }

    public List<Program> findAllPurchasedPrograms(String email) {
        return programRepository.findAllPurchasedAndNotExpiredPrograms(email, LocalDateTime.now());
    }

    public void delete(Long id) {
        programRepository.deleteById(id);
    }

}
