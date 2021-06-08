package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.entity.FolderItem;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.entity.ViewedFolderItem;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
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

    public List<Folder> findAllByProgramIdOrderByOrdering(Long programId) {
        return folderRepository.findAllByProgramIdOrderByOrdering(programId);
    }

    public Folder findById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.FOLDER_NOT_FOUND, id));
    }

    public Folder findByIdFetchItems(Long id) {
        return folderRepository.findFetchItemsByIdOrderByItemsOrdering(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.FOLDER_NOT_FOUND, id));
    }

    public Folder findByIdFetchItemsAndViewedFolderItems(Long id) {
        Folder folder = folderRepository.findFetchItemsByIdOrderByItemsOrdering(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.FOLDER_NOT_FOUND, id));
        List<Long> folderItemIds = folder.getItems().stream()
                .map(FolderItem::getId)
                .collect(Collectors.toList());
        folderItemService.findAllFetchViewedFolderItemsByIdIn(folderItemIds);
        return folder;
    }

    public Folder save(Folder program) {
        return folderRepository.save(program);
    }

    public boolean existsByNameAndProgram(String name, Program program) {
        return folderRepository.existsByNameAndProgram(name, program);
    }

    public boolean existsByNameAndProgramAndIdNot(String name, Program program, Long folderId) {
        return folderRepository.existsByNameAndProgramAndIdNot(name, program, folderId);
    }

    public void delete(Long id) {
        folderRepository.deleteById(id);
    }

    public void resetFolderProgress(Long userId, Long folderId) {
        Folder folder = folderRepository.findFetchItemsByIdOrderByItemsOrdering(folderId)
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
