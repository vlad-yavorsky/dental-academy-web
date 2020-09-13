package ua.kazo.dentalacademy.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class OrderHistory extends TrackedDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_history_id_seq")
    @SequenceGenerator(name = "order_history_id_seq", sequenceName = "order_history_id_seq", allocationSize = 1)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private UnifiedPaymentStatus status;

    private String data;

}
