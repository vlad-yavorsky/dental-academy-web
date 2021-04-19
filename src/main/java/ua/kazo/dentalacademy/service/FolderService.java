package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ua.kazo.dentalacademy.entity.*;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.FolderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FolderService {

    private final FolderRepository folderRepository;
    private final MessageSource messageSource;
    private final FolderItemService folderItemService;
    private final ViewedFolderItemService viewedFolderItemService;

    public List<Folder> findAllByProgramId(Long programId) {
        return folderRepository.findAllByPrograms_Id(programId);
    }

    public Folder findById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.FOLDER_NOT_FOUND, id));
    }

    public Folder findByIdAndCategoryFetchItems(Long id, FolderCategory category) {
        return folderRepository.findFetchItemsByIdAndCategory(id, category)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.FOLDER_NOT_FOUND, id));
    }

    public List<Folder> findAllByCategory(FolderCategory category) {
        return folderRepository.findAllByCategory(category);
    }

    public Page<Folder> findAllByCategory(FolderCategory category, Pageable pageable) {
        return folderRepository.findAllByCategory(category, pageable);
    }

    public Page<Folder> findAllByUserEmail(String userEmail, String search, Pageable pageable) {
        if (!ObjectUtils.isEmpty(search)) {
            return folderRepository.findAllByUserEmailAndName(userEmail, search, UnifiedPaymentStatus.SUCCESS, pageable);
        }
        return folderRepository.findAllByUserEmail(userEmail, UnifiedPaymentStatus.SUCCESS, pageable);
    }

    public Folder findByIdFetchItems(Long id) {
        Folder folder = folderRepository.findByIdFetchItems(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.FOLDER_NOT_FOUND, id));
        List<Long> folderItemIds = folder.getItems().stream()
                .map(FolderItem::getId)
                .collect(Collectors.toList());
        folderItemService.findAllFetchViewedFolderItemsByIdIn(folderItemIds);
        return folder;
    }

    public Folder findByIdFetchItemsAndPrograms(Long id) {
        Folder folder = folderRepository.findByIdFetchItems(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.FOLDER_NOT_FOUND, id));
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

    public void resetFolderProgress(Long userId, Long folderId) {
        Folder folder = folderRepository.findByIdFetchItems(folderId)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.FOLDER_NOT_FOUND, folderId));
        List<Long> folderItemIds = folder.getItems().stream()
                .map(FolderItem::getId)
                .collect(Collectors.toList());
        List<ViewedFolderItem> viewedFolderItems = folderItemIds.stream()
                .map(folderItemId -> ViewedFolderItem.of(userId, folderItemId))
                .collect(Collectors.toList());
        viewedFolderItemService.deleteAll(viewedFolderItems);
    }

}
