package sportbets.service;

import sportbets.web.dto.TeamDto;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    Optional<TeamDto> findById(Long id);

    Optional<TeamDto> save(TeamDto compFam);

    Optional<TeamDto> updateTeam(Long id, TeamDto compFam);

    void deleteById(Long id);
    void deleteByName(String name );
    List<TeamDto> getAll();
}
