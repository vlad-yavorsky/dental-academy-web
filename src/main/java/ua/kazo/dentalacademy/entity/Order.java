package ua.kazo.dentalacademy.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.kazo.dentalacademy.constants.Graph;
import ua.kazo.dentalacademy.enumerated.PaymentProvider;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NamedEntityGraph(
        name = Graph.ORDER_PURCHASE_DATA_OFFERING,
        attributeNodes = {
                @NamedAttributeNode(value = "purchaseData", subgraph = "purchaseData.offering")
        },
        subgraphs = {
                @NamedSubgraph(name = "purchaseData.offering", attributeNodes = @NamedAttributeNode(value = "offering"))
        })
@NamedEntityGraph(
        name = Graph.ORDER_PURCHASE_DATA_OFFERING_AND_USER_WITH_ROLES,
        attributeNodes = {
                @NamedAttributeNode(value = "purchaseData", subgraph = "purchaseData.offering"),
                @NamedAttributeNode(value = "user", subgraph = "user.roles")
        },
        subgraphs = {
                @NamedSubgraph(name = "purchaseData.offering", attributeNodes = @NamedAttributeNode(value = "offering")),
                @NamedSubgraph(name = "user.roles", attributeNodes = @NamedAttributeNode(value = "roles"))
        })
public class Order extends TrackedDateEntity implements Serializable {

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
    private UnifiedPaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentProvider provider;

    private LocalDateTime purchased;

    private BigDecimal price;

}
