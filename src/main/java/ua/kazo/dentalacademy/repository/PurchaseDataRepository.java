package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.PurchaseData;
import ua.kazo.dentalacademy.entity.PurchaseDataId;
import ua.kazo.dentalacademy.enumerated.LiqPayPaymentStatus;

import java.time.LocalDateTime;

@Repository
public interface PurchaseDataRepository extends JpaRepository<PurchaseData, PurchaseDataId> {

    /**
     * Client / Permission Evaluator, Shop Item
     */
    @Query("select (count(pd) > 0) from PurchaseData pd " +
            "join pd.offering o " +
            "join o.programs p " +
            "where p.id = :programId and pd.expired > :dateTime and pd.order.user.email = :email and pd.order.status = :status ")
    boolean isProgramPurchasedAndNotExpired(Long programId, LocalDateTime dateTime, String email, LiqPayPaymentStatus status);

    /**
     * Client / Permission Evaluator
     */
    @Query("select (count(pd) > 0) from PurchaseData pd " +
            "join pd.offering o " +
            "join o.programs p " +
            "left join p.folders module " +
            "left join o.folders bonus " +
            "where (module.id = :folderId or bonus.id = :folderId) and pd.expired > :dateTime and pd.order.user.email = :email and pd.order.status = :status ")
    boolean isFolderPurchasedAndNotExpired(Long folderId, LocalDateTime dateTime, String email, LiqPayPaymentStatus status);

}
