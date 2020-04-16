package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.enumerated.OfferingType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfferingRepository extends JpaRepository<Offering, Long> {

    boolean existsByNameAndType(String name, OfferingType type);
    boolean existsByNameAndTypeAndIdNot(String name, OfferingType type, Long id);

    /**
     * Admin side: Offering Edit page
     */
    @Query("select o from Offering o " +
            "left join fetch o.programs p " +
            "where o.id = :id")
    Optional<Offering> findByIdFetchPrograms(Long id);

    /**
     * Admin side: Offering Edit page
     */
    @Query("select o from Offering o " +
            "left join fetch o.folders f " +
            "where o.id = :id")
    Optional<Offering> findByIdFetchFolders(Long id);

    /**
     * Admin side: Program Edit page
     */
    @Query("select o from Offering o " +
            "join o.programs p " +
            "where p.id = :programId")
    List<Offering> findAllByProgramId(Long programId);

    /**
     * Client side: Shop Item page
     */
    @Query("select o.id from Offering o " +
            "join o.programs p " +
            "where p.id = :programId")
    List<Long> findAllIdsByProgramId(Long programId);

    /**
     * Client side: Shop Item page
     */
    @Query("select distinct o from Offering o " +
            "left join fetch o.programs p " +
            "where o.id in (:ids) and (o.deactivated is null or :dateTime < o.deactivated)")
    List<Offering> findAllByIdsAndNotDeactivatedFetchPrograms(List<Long> ids, LocalDateTime dateTime);

    /**
     * Client side: Shop Item page
     */
    @Query("select distinct o from Offering o " +
            "left join fetch o.folders f " +
            "where o.id in (:ids) and (o.deactivated is null or :dateTime < o.deactivated)")
    List<Offering> findAllByIdsAndNotDeactivatedFetchFolders(List<Long> ids, LocalDateTime dateTime);

    /**
     * Client side: Buy Offering Process
     */
    @Query("select o from Offering o " +
            "where o.id = :id and o.activated < :dateTime and (o.deactivated is null or :dateTime < o.deactivated)")
    Optional<Offering> findByIdAndActive(Long id, LocalDateTime dateTime);

}
