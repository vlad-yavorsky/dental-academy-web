package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.ViewedFolderItem;
import ua.kazo.dentalacademy.repository.ViewedFolderItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ViewedFolderItemService {

    private final ViewedFolderItemRepository viewedFolderItemRepository;

    public void setFolderItemIsViewedByUser(Long userId, Long folderItemId) {
        viewedFolderItemRepository.save(ViewedFolderItem.of(userId, folderItemId));
    }

    public void deleteAll(List<ViewedFolderItem> viewedFolderItems) {
        viewedFolderItemRepository.deleteAll(viewedFolderItems);
    }

}
