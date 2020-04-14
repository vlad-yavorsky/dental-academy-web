package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.enumerated.FolderCategory;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.entity.Program;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    boolean existsByNameAndProgramsIn(String name, List<Program> programs);
    boolean existsByNameAndProgramsInAndIdNot(String name, List<Program> programs, Long id);
    boolean existsByNameAndOfferingsIn(String name, List<Offering> offerings);
    boolean existsByNameAndOfferingsInAndIdNot(String name, List<Offering> offerings, Long id);

    /**
     * Client side: Folder Items page
     * Admin side: Edit Folder page
     */
    @Query("select f from Folder f " +
            "join fetch f.items i " +
            "where f.id = :id " +
            "order by i.ordering")
    Optional<Folder> findByIdFetchItems(Long id);

    /**
     * Admin side: Edit Folder page
     */
    @Query("select f from Folder f " +
            "left join fetch f.programs p " +
            "where f.id = :id")
    Optional<Folder> findByIdFetchPrograms(Long id);

    /**
     * Admin side: Edit Program page
     */
    List<Folder> findAllByPrograms_Id(Long programId);

    /**
     * Admin side: Edit Offering page; Bonuses page
     */
    List<Folder> findAllByCategory(FolderCategory category);

    /**
     * Client side: My Bonuses page
     */
    @Query("select distinct f from Folder f " +
            "join f.offerings o " +
            "join o.purchaseData pd " +
            "where pd.user.email = :userEmail " +
            "order by f.id")
    List<Folder> findAllByUserEmail(String userEmail);

}
