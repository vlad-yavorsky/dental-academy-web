package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.FolderItem;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.FolderItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FolderItemService {

    private final FolderItemRepository folderItemRepository;
    private final MessageSource messageSource;

    public FolderItem findById(Long id) {
        return folderItemRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.FOLDER_ITEM_NOT_FOUND, id));
    }

    public List<FolderItem> findAllFetchViewedFolderItemsByIdIn(List<Long> ids) {
        return folderItemRepository.findAllFetchViewedFolderItemsByIdIn(ids);
    }

}
