package ua.kazo.dentalacademy.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.kazo.dentalacademy.enumerated.OfferingType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Offering extends TrackedDateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offering_id_seq")
    @SequenceGenerator(name = "offering_id_seq", sequenceName = "offering_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    private String name;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private OfferingType type;

    private BigDecimal price;

    private Byte discount;

    private byte term;

    private LocalDateTime activated;

    private LocalDateTime deactivated;

    @ToString.Exclude
    @OneToMany(mappedBy = "offering")
    private List<PurchaseData> purchaseData = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "offering_program",
            joinColumns = {@JoinColumn(name = "offeringId")},
            inverseJoinColumns = {@JoinColumn(name = "programId")})
    private List<Program> programs;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "offering_bonus",
            joinColumns = {@JoinColumn(name = "offeringId")},
            inverseJoinColumns = {@JoinColumn(name = "programId")})
    private List<Program> bonuses;

    @ToString.Exclude
    @ManyToMany(mappedBy = "cartItems")
    private List<User> usersAddedToCart;

}
