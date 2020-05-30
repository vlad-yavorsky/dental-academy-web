package ua.kazo.dentalacademy.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.kazo.dentalacademy.constants.Graph;
import ua.kazo.dentalacademy.enumerated.LiqPayPaymentStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"user"})
@NamedEntityGraph(name = Graph.ORDER_PURCHASE_DATA_OFFERING,
        attributeNodes = @NamedAttributeNode(value = "purchaseData", subgraph = "purchaseData.offering"),
        subgraphs = @NamedSubgraph(name = "purchaseData.offering", attributeNodes = @NamedAttributeNode(value = "offering")))
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
    @SequenceGenerator(name = "order_id_seq", sequenceName = "order_id_seq", allocationSize = 1)
    private Long id;

    private String number;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<PurchaseData> purchaseData = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private LiqPayPaymentStatus status;

    private LocalDateTime purchased;

    private BigDecimal price;

}
