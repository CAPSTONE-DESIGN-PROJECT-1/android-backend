package backend.backendspring.src.statistic;

import backend.backendspring.src.statistic.model.entity.StatisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticRepository extends JpaRepository<StatisticEntity, Long> {

    Optional<StatisticEntity> findByPaperEntity_PaperIdxAndStatus(Long paperIdx, String status);
    Optional<List<StatisticEntity>> findByUserEntity_UserIdxAndStatus(Long userIdx, String status);

}
