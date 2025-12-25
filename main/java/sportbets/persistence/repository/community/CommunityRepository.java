package sportbets.persistence.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import sportbets.persistence.entity.community.Community;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findByName(String name);
    void deleteByName(String name);
}