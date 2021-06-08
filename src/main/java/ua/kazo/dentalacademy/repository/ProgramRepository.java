package ua.kazo.dentalacademy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.enumerated.ProgramCategory;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    /**
     * Admin / Offering Add; Offering Edit
     */
    @EntityGraph(attributePaths = "folders")
    List<Program> findAllJoinFoldersByCategoryOrderByName(ProgramCategory category);

    /**
     * Admin / Programs; Bonuses
     */
    Page<Program> findAllByCategory(ProgramCategory category, Pageable pageable);

    /**
     * Client / Shop
     */
    @EntityGraph(attributePaths = "offerings")
    @Query("select distinct p from Program p " +
            "join p.offerings o " +
            "where (o.deactivated is null or :dateTime < o.deactivated) " +
            "and (:name is null or (lower(p.name) like lower(concat('%', concat(cast(:name as string), '%'))))) " +
            "order by p.id desc")
    Page<Program> findAllByNotDeactivatedOfferingsAndName(LocalDateTime dateTime, String name, Pageable pageable);

    @EntityGraph(attributePaths = "folders")
    Optional<Program> findFetchFoldersByIdOrderByFoldersOrdering(Long id);

    /**
     * Client / My Programs
     */
    @Query("select distinct p from Program p " +
            "join p.offerings o " +
            "join o.purchaseData pd " +
            "join pd.order ord " +
            "where pd.expired > :dateTime and pd.order.user.email = :email and ord.status = :status " +
            "and (:name is null or (lower(p.name) like lower(concat('%', concat(cast(:name as string), '%')))))")
    Page<Program> findAllProgramsByNotExpiredPurchaseAndName(String email, LocalDateTime dateTime, String name, Pageable pageable, UnifiedPaymentStatus status);

    /**
     * Client / My Bonuses
     */
    @Query("select distinct p from Program p " +
            "join p.offeringsByBonuses o " +
            "join o.purchaseData pd " +
            "join pd.order ord " +
            "where pd.expired > :dateTime and pd.order.user.email = :email and ord.status = :status " +
            "and (:name is null or (lower(p.name) like lower(concat('%', concat(cast(:name as string), '%')))))")
    Page<Program> findAllBonusesByNotExpiredPurchaseAndName(String email, LocalDateTime dateTime, String name, Pageable pageable, UnifiedPaymentStatus status);

    int countByCategory(ProgramCategory category);

}
