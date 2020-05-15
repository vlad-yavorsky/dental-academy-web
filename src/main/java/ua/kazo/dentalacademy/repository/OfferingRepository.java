package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.enumerated.OfferingType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OfferingRepository extends JpaRepository<Offering, Long> {

    boolean existsByNameAndType(String name, OfferingType type);
    boolean existsByNameAndTypeAndIdNot(String name, OfferingType type, Long id);

    /**
     * Admin / Offering Edit
     */
    @EntityGraph(attributePaths = "programs")
    Optional<Offering> findFetchProgramsById(Long id);

    /**
     * Admin / Offering Edit
     */
    @EntityGraph(attributePaths = "folders")
    Optional<Offering> findFetchFoldersById(Long id);

    /**
     * Admin / Program Edit
     */
    @Query("select o from Offering o " +
            "join o.programs p " +
            "where p.id = :programId")
    List<Offering> findAllByProgramId(Long programId);

    /**
     * Client / Shop Item
     */
    @Query("select o.id from Offering o " +
            "join o.programs p " +
            "where p.id = :programId")
    List<Long> findAllIdsByProgramId(Long programId);

    /**
     * Client / Shop Item
     */
    @Query("select distinct o from Offering o " +
            "left join fetch o.programs " +
            "where o.id in (:ids) and (o.deactivated is null or :dateTime < o.deactivated)")
    List<Offering> findAllByIdsAndNotDeactivatedFetchPrograms(List<Long> ids, LocalDateTime dateTime);

    /**
     * Client / Shop Item
     */
    @Query("select distinct o from Offering o " +
            "left join fetch o.folders " +
            "where o.id in (:ids) and (o.deactivated is null or :dateTime < o.deactivated)")
    List<Offering> findAllByIdsAndNotDeactivatedFetchFolders(List<Long> ids, LocalDateTime dateTime);

    /**
     * Client / Add offering to cart
     */
    @Query("select o from Offering o " +
            "where o.id = :id and o.activated < :dateTime and (o.deactivated is null or :dateTime < o.deactivated)")
    Optional<Offering> findByIdAndActive(Long id, LocalDateTime dateTime);

    /**
     * Client / Order History
     */
    @EntityGraph(attributePaths = "programs")
    List<Offering> findAllFetchProgramsByIdIn(Set<Long> ids);

    /**
     * Client / Order History
     */
    @EntityGraph(attributePaths = "folders")
    List<Offering> findAllFetchFoldersByIdIn(Set<Long> ids);

}
