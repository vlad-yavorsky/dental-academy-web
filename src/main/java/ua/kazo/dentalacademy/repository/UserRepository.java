package ua.kazo.dentalacademy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u " +
            "where u.activationToken = :activationToken " +
            "and :now < u.tokenExpiryDate")
    Optional<User> findByActivationTokenAndTokenNotExpired(String activationToken, LocalDateTime now);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Admin / User
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findFetchRolesById(Long id);

    /**
     * Client / Profile Update
     * Client / Password Update
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findFetchRolesByEmail(String email);

    /**
     * Admin / Users
     */
    @EntityGraph(attributePaths = "roles")
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "cartItems")
    Optional<User> findFetchCartItemsByEmail(String email);

    /**
     * Client / Shop Item
     */
    @EntityGraph(attributePaths = "orders")
    Optional<User> findFetchOrdersByEmail(String email);

    /**
     * Rest API / Whole website
     */
    @Query("select count(ci.id) from User u join u.cartItems ci where u.email = :email")
    int countCartItems(String email);

}
