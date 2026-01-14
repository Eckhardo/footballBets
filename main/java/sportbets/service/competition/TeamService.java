package sportbets.service.competition;

import sportbets.persistence.entity.competition.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    Optional<Team> findById(Long id);

    Team save(Team team);

    Optional<Team> updateTeam(Long id, Team team);

    void deleteById(Long id);

    void deleteByName(String name);

    List<Team> getAll();

    Optional<Team> findByName(String name);

    List<Team> getAllClubTeams();
    List<Team> getAllNationTeams();
}
