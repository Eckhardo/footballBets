package sportbets.persistence.repository.competition;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.competition.Team;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<Team> findByName(String name);

    void deleteByName(String name);

    @Query(" select t from Team t where t.hasClub=true")
    List<Team> findAllClubTeams();

    @Query(" select t from Team t where t.hasClub=false")
    List<Team> findAllNationTeams();
}
