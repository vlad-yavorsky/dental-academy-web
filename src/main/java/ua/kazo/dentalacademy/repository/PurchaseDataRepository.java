package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.PurchaseData;
import ua.kazo.dentalacademy.entity.PurchaseDataId;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseDataRepository extends JpaRepository<PurchaseData, PurchaseDataId> {

    /**
     * Client side: Purchase Data page
     */
    @Query("select distinct ph from PurchaseData ph " +
            "left join fetch ph.offering o " +
            "left join fetch o.programs " +
            "where ph.user.email = :email " +
            "order by ph.purchased desc")
    List<PurchaseData> findAllByUserEmailFetchOfferingAndPrograms(String email);

    /**
     * Client side: Purchase Data page
     */
    @Query("select distinct ph from PurchaseData ph " +
            "left join fetch ph.offering o " +
            "left join fetch o.folders " +
            "where ph.user.email = :email " +
            "order by ph.purchased desc")
    List<PurchaseData> findAllByUserEmailFetchOfferingAndFolders(String email);

    /**
     * Client side: Buy Offering Process
     */
    boolean existsByIdOfferingIdAndUserEmail(Long offeringId, String email);

    /**
     * Client side: Shop Item page
     */
    List<PurchaseData> findAllByIdOfferingIdInAndUserEmail(List<Long> offeringIds, String userEmail);

    /**
     * Client side: Permission Evaluator, Shop Item page
     */
    @Query("select (count(pd) > 0) from PurchaseData pd " +
            "join pd.offering o " +
            "join o.programs p " +
            "where p.id = :programId and pd.expired > :dateTime and pd.user.email = :email")
    boolean isProgramPurchasedAndNotExpired(Long programId, LocalDateTime dateTime, String email);

    /**
     * Client side: Permission Evaluator
     */
    @Query("select (count(pd) > 0) from PurchaseData pd " +
            "join pd.offering o " +
            "join o.programs p " +
            "left join p.folders module " +
            "left join o.folders bonus " +
            "where (module.id = :folderId or bonus.id = :folderId) and pd.expired > :dateTime and pd.user.email = :email")
    boolean isFolderPurchasedAndNotExpired(Long folderId, LocalDateTime dateTime, String email);

}
