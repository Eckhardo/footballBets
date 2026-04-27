package sportbets.service.competition;

import sportbets.web.dto.competition.TeamDto;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    Optional<TeamDto> findById(Long id);

    TeamDto save(TeamDto teamDto);

    Optional<TeamDto> updateTeam(Long id, TeamDto teamDto);

    void deleteById(Long id);

    void deleteByName(String name);

    List<TeamDto> getAll();

    Optional<TeamDto> findByName(String name);

    List<TeamDto> getAllClubTeams();
    List<TeamDto> getAllNationTeams();
}
