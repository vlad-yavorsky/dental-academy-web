package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.repository.FolderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FolderService {

    private final FolderRepository folderRepository;

    public List<Folder> findAll() {
        return folderRepository.findAll(Sort.by("id"));
    }

    public List<Folder> findAllByProgramId(Long programId) {
        return folderRepository.findAllByPrograms_Id(programId);
    }

    public Folder findById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.FOLDER_ITEM_NOT_FOUND, id));
    }

    public List<Folder> findAllByCategory(FolderCategory category) {
        return folderRepository.findAllByCategory(category);
    }

    public List<Folder> findAllByUserEmail(String userEmail) {
        return folderRepository.findAllByUserEmail(userEmail);
    }

    public Folder findByIdFetchItems(Long id) {
        return folderRepository.findByIdFetchItems(id)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.FOLDER_NOT_FOUND));
    }

    public Folder findByIdFetchItemsAndPrograms(Long id) {
        Folder folder = folderRepository.findByIdFetchItems(id)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.FOLDER_NOT_FOUND));
        folderRepository.findByIdFetchPrograms(id);
        return folder;
    }

    public Folder save(Folder program) {
        return folderRepository.save(program);
    }

    public boolean existsByNameAndPrograms(String name, List<Program> programs) {
        return folderRepository.existsByNameAndProgramsIn(name, programs);
    }
    public boolean existsByNameAndProgramsAndIdNot(String name, List<Program> programs, Long folderId) {
        return folderRepository.existsByNameAndProgramsInAndIdNot(name, programs, folderId);
    }
    public boolean existsByNameAndOfferings(String name, List<Offering> offerings) {
        return folderRepository.existsByNameAndOfferingsIn(name, offerings);
    }
    public boolean existsByNameAndOfferingsAndIdNot(String name, List<Offering> offerings, Long folderId) {
        return folderRepository.existsByNameAndOfferingsInAndIdNot(name, offerings, folderId);
    }

    public void delete(Long id) {
        folderRepository.deleteById(id);
    }

}
