package ua.kazo.dentalacademy.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.kazo.dentalacademy.enumerated.OfferingType;
import ua.kazo.dentalacademy.util.MathUtil;

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

    private String name;

    @Enumerated(EnumType.STRING)
    private OfferingType type;

    private BigDecimal price;

    private LocalDateTime purchased;

    private LocalDateTime expired;

    private PurchaseData(Offering offering, User user, LocalDateTime dateTime) {
        this.id = new PurchaseDataId(offering.getId(), user.getId());
        this.offering = offering;
        this.user = user;
        this.purchased = dateTime;
        this.expired = dateTime.plusMonths(offering.getTerm());
        this.price = MathUtil.calculateDiscountPrice(offering.getPrice(), offering.getDiscount());
        this.name = offering.getName();
        this.type = offering.getType();
    }

    public static PurchaseData of(Offering offering, User user, LocalDateTime dateTime) {
        return new PurchaseData(offering, user, dateTime);
    }

}
