package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.FolderItem;

import java.util.List;

@Repository
public interface FolderItemRepository extends JpaRepository<FolderItem, Long> {

    @EntityGraph(attributePaths = "viewedFolderItems")
    List<FolderItem> findAllFetchViewedFolderItemsByIdIn(List<Long> ids);

}
