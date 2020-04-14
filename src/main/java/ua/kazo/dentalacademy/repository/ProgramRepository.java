package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.enumerated.FolderCategory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    /**
     * Client side: Shop page
     */
    @Query("select distinct p from Program p " +
            "join p.offerings o " +
            "where (o.deactivated is null or o.deactivated > :dateTime) " +
            "order by p.id desc")
    List<Program> findAllWithActiveOfferings(LocalDateTime dateTime);

    /**
     * Client side: Program Modules/QA page
     */
    @Query("select (count(p) > 0) from Program p " +
            "join p.folders f " +
            "where p.id = :id and f.category = :category")
    boolean existByIdAndFolderCategory(Long id, FolderCategory category);

    /**
     * Client side: Program Modules/QA page
     */
    @Query("select distinct p from Program p " +
            "join fetch p.folders f " +
            "where p.id = :id and f.category = :category " +
            "order by f.id")
    Optional<Program> findByIdAndFolderCategoryFetchFolders(Long id, FolderCategory category);

    /**
     * Client side: Permission Evaluator
     */
    @Query("select (count(p) > 0) from Program p " +
            "join p.offerings o " +
            "join o.purchaseData pd " +
            "where p.id = :id and pd.expired > :dateTime and pd.user.email = :email")
    boolean isProgramPurchasedAndNotExpired(Long id, String email, LocalDateTime dateTime);

    /**
     * Client side: Permission Evaluator
     */
    @Query("select (count(p) > 0) from Program p " +
            "join p.offerings o " +
            "join o.purchaseData pd " +
            "left join o.folders f1 " +
            "left join p.folders f2 " +
            "where (f1.id = :folderId or f2 = :folderId) and pd.expired > :dateTime and pd.user.email = :email")
    boolean isFolderPurchasedAndNotExpired(Long folderId, String email, LocalDateTime dateTime);

    /**
     * Client side: My Programs page
     */
    @Query("select distinct p from Program p " +
            "join p.offerings o " +
            "join o.purchaseData pd " +
            "where pd.expired > :dateTime and pd.user.email = :email " +
            "order by p.id desc")
    List<Program> findAllPurchasedAndNotExpiredPrograms(String email, LocalDateTime dateTime);

}
