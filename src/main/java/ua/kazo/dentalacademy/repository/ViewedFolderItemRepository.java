package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.ViewedFolderItem;
import ua.kazo.dentalacademy.entity.ViewedFolderItemId;

@Repository
public interface ViewedFolderItemRepository extends JpaRepository<ViewedFolderItem, ViewedFolderItemId> {
}
