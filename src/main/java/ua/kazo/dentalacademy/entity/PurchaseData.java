package ua.kazo.dentalacademy.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.kazo.dentalacademy.enumerated.OfferingType;
import ua.kazo.dentalacademy.util.MathUtil;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"offering", "order"})
public class PurchaseData implements Serializable {

    @EmbeddedId
    private PurchaseDataId id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("offeringId")
    private Offering offering;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("orderId")
    private Order order;

    private String name;

    @Enumerated(EnumType.STRING)
    private OfferingType type;

    private BigDecimal price;

    private LocalDateTime expired;

    private PurchaseData(Offering offering, Order order) {
        this.id = new PurchaseDataId(offering.getId(), order.getId());
        this.offering = offering;
        this.order = order;
        this.price = MathUtil.calculateDiscountPrice(offering.getPrice(), offering.getDiscount());
        this.name = offering.getName();
        this.type = offering.getType();
    }

    public static PurchaseData of(Offering offering, Order order) {
        return new PurchaseData(offering, order);
    }

}
