package ua.kazo.dentalacademy.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;
import ua.kazo.dentalacademy.enumerated.Role;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class User extends TrackedDateEntity implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private boolean enabled;

    private boolean accountNonLocked;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String mobile;

    private LocalDate birthday;

    private String photoName;

    @ToString.Exclude
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "cart_item",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "offeringId")})
    private List<Offering> cartItems = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<ViewedFolderItem> viewedFolderItems = new ArrayList<>();

    @Transient
    private int cartItemsCount;

    private String interests;

    private String activationToken;

    private LocalDateTime tokenExpiryDate;

    public void incCartItemsCount() {
        cartItemsCount++;
    }

    public void decCartItemsCount() {
        cartItemsCount--;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return roles;
    }

    public String getUsername() {
        return email;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

}
