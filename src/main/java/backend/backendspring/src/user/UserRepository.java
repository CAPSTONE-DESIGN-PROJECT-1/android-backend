package backend.backendspring.src.user;

import backend.backendspring.src.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUniqueIdAndStatus(String uniqueId, String status);

    Optional<UserEntity> findByUserIdxAndStatus(Long userIdx, String status);

}
