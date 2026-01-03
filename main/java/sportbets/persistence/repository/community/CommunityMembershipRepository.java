package sportbets.persistence.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.community.CommunityMembership;

import java.util.Optional;
@Repository
public interface CommunityMembershipRepository extends JpaRepository<CommunityMembership, Long> {

    @Query(" select cm from CommunityMembership cm join cm.tipper t join cm.community c " +
            "where t.id=:tipperId and c.id=:commId  ")
    Optional<CommunityMembership> findByCommIdAndTipperId(Long commId, Long tipperId);
}