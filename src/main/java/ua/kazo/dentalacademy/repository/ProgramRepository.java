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

    @Query("select distinct p from Program p " +
            "join p.folders " +
            "order by p.name")
    List<Program> findAllWithFolders();

    /**
     * Client / Shop
     */
    @Query("select distinct p from Program p " +
            "join p.offerings o " +
            "where (o.deactivated is null or :dateTime < o.deactivated) " +
            "order by p.id desc")
    List<Program> findAllByNotDeactivatedOfferings(LocalDateTime dateTime);

    /**
     * Client / Shop (search)
     */
    @Query("select distinct p from Program p " +
            "join p.offerings o " +
            "where (o.deactivated is null or :dateTime < o.deactivated) " +
            "and lower(p.name) like lower(concat('%', concat(:name, '%'))) " +
            "order by p.id desc")
    List<Program> findAllByNotDeactivatedOfferingsAndName(LocalDateTime dateTime, String name);

    /**
     * Client / My Programs / Modules, QA
     */
    @Query("select (count(p) > 0) from Program p " +
            "join p.folders f " +
            "where p.id = :id and f.category = :category")
    boolean existByIdAndFolderCategory(Long id, FolderCategory category);

    /**
     * Client / My Programs / Modules, QA
     */
    @Query("select distinct p from Program p " +
            "join fetch p.folders f " +
            "where p.id = :id and f.category = :category " +
            "order by f.id")
    Optional<Program> findByIdAndFolderCategoryFetchFolders(Long id, FolderCategory category);

    /**
     * Client / My Programs
     */
    @Query("select distinct p from Program p " +
            "join p.offerings o " +
            "join o.purchaseData pd " +
            "where pd.expired > :dateTime and pd.user.email = :email " +
            "order by p.id desc")
    List<Program> findAllByNotExpiredPurchase(String email, LocalDateTime dateTime);

    /**
     * Client / My Programs (search)
     */
    @Query("select distinct p from Program p " +
            "join p.offerings o " +
            "join o.purchaseData pd " +
            "where pd.expired > :dateTime and pd.user.email = :email " +
            "and lower(p.name) like lower(concat('%', concat(:name, '%'))) " +
            "order by p.id desc")
    List<Program> findAllByNotExpiredPurchaseAndName(String email, LocalDateTime dateTime, String name);

}
