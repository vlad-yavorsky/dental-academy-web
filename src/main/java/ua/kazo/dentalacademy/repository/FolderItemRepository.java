package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.FolderItem;

@Repository
public interface FolderItemRepository extends JpaRepository<FolderItem, Long> {
}
