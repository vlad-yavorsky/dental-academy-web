package ua.kazo.dentalacademy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    boolean existsByNameAndProgramsIn(String name, List<Program> programs);
    boolean existsByNameAndProgramsInAndIdNot(String name, List<Program> programs, Long id);
    boolean existsByNameAndOfferingsIn(String name, List<Offering> offerings);
    boolean existsByNameAndOfferingsInAndIdNot(String name, List<Offering> offerings, Long id);

    /**
     * Client / My Programs / Folder Items, Bonus Items
     * Admin / Edit Folder
     */
    @Query("select f from Folder f " +
            "join fetch f.items i " +
            "where f.id = :id " +
            "order by i.ordering")
    Optional<Folder> findByIdFetchItems(Long id);

    /**
     * Admin / Edit Folder
     */
    @Query("select f from Folder f " +
            "left join fetch f.programs p " +
            "where f.id = :id")
    Optional<Folder> findByIdFetchPrograms(Long id);

    /**
     * Admin / Edit Program
     */
    List<Folder> findAllByPrograms_Id(Long programId);

    /**
     * Admin / Add Offering, Edit Offering
     */
    List<Folder> findAllByCategory(FolderCategory category);

    /**
     * Admin / Bonuses
     */
    Page<Folder> findAllByCategory(FolderCategory category, Pageable pageable);

    /**
     * Client / My Bonuses
     */
    @Query("select distinct f from Folder f " +
            "join f.offerings o " +
            "join o.purchaseData pd " +
            "where pd.order.user.email = :userEmail and pd.order.status = :status " +
            "order by f.id")
    Page<Folder> findAllByUserEmail(String userEmail, UnifiedPaymentStatus status, Pageable pageable);

    /**
     * Client / My Bonuses (search)
     */
    @Query("select distinct f from Folder f " +
            "join f.offerings o " +
            "join o.purchaseData pd " +
            "where pd.order.user.email = :userEmail and pd.order.status = :status " +
            "and lower(f.name) like lower(concat('%', concat(:name, '%'))) " +
            "order by f.id")
    Page<Folder> findAllByUserEmailAndName(String userEmail, String name, UnifiedPaymentStatus status, Pageable pageable);

}
