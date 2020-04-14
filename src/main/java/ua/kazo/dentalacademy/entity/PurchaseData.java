package ua.kazo.dentalacademy.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"user", "offering"})
public class PurchaseData implements Serializable {

    @EmbeddedId
    private PurchaseDataId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("offeringId")
    private Offering offering;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    private User user;

    private LocalDateTime purchased;

    private LocalDateTime expired;

    private BigDecimal price;

    private PurchaseData(Offering offering, User user, LocalDateTime purchased, LocalDateTime expired, BigDecimal price) {
        this.id = new PurchaseDataId(offering.getId(), user.getId());
        this.offering = offering;
        this.user = user;
        this.purchased = purchased;
        this.expired = expired;
        this.price = price;
    }

    public static PurchaseData of(Offering offering, User user, LocalDateTime purchased, LocalDateTime expired, BigDecimal price) {
        return new PurchaseData(offering, user, purchased, expired, price);
    }

}
