package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.entity.Program;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    boolean existsByNameAndProgram(String name, Program program);
    boolean existsByNameAndProgramAndIdNot(String name, Program program, Long id);

    /**
     * Client / My Programs / Folder Items, Bonus Items
     * Admin / Edit Folder
     */

    @Query("""
            select folder
            from Folder folder
                 left join fetch folder.items item
            where folder.id = :id
            order by item.ordering
            """)
    Optional<Folder> findFetchItemsByIdOrderByItemsOrdering(Long id);

    /**
     * Admin / Edit Program
     */
    List<Folder> findAllByProgramIdOrderByOrdering(Long programId);

}
