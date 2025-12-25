package sportbets.persistence.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.community.Tipper;

import java.util.Optional;

@Repository
public interface TipperRepository extends JpaRepository<Tipper, Long> {
    Optional<Tipper> findByUsername(String username);

    void deleteByUsername(String username);
}