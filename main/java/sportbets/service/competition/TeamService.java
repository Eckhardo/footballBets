package sportbets.service.competition;

import sportbets.persistence.entity.competition.Team;
import sportbets.web.dto.competition.TeamDto;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    Optional<TeamDto> findById(Long id);

    Optional<TeamDto> save(TeamDto compFam);

    Optional<TeamDto> updateTeam(Long id, TeamDto compFam);

    void deleteById(Long id);

    void deleteByName(String name);

    List<TeamDto> getAll();

    Optional<Team> findByName(String name);
}
