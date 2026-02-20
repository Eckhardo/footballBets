package sportbets.persistence.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.community.Community;

import java.util.Optional;
@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findByName(String name);

    void deleteByName(String name);
}