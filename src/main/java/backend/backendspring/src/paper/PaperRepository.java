package backend.backendspring.src.paper;

import backend.backendspring.src.paper.model.entity.PaperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaperRepository extends JpaRepository<PaperEntity, Long> {

    Optional<List<PaperEntity>> findByStatus(String status);

    Optional<PaperEntity> findByPaperIdxAndStatus(Long paperIdx, String status);

    @Query(value = "select * from paper p " +
            "where p.status = ?1 and p.category_num = ?2 " +
            "order by RAND() limit 5 ;", nativeQuery = true)
    Optional<List<PaperEntity>> findAll(String status, String categoryNum);

}
