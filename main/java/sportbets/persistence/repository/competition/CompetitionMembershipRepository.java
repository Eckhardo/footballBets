package sportbets.persistence.repository.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.CompetitionMembership;

import java.util.Optional;
@Repository
public interface CompetitionMembershipRepository extends JpaRepository<CompetitionMembership, Long> {


    @Query(" select cm  from CompetitionMembership cm join cm.community comm join cm.competition comp" +
            " where comm.id= :commId and comp.id= :compId")
    Optional<CompetitionMembership> findByCommIdAndCompId(Long commId, Long compId);
}