package sportbets.service;

import org.springframework.transaction.annotation.Transactional;
import sportbets.web.dto.TeamDto;

import java.util.List;
import java.util.Optional;
@Transactional
public interface TeamService {
    Optional<TeamDto> findById(Long id);

    Optional<TeamDto> save(TeamDto compFam);

    Optional<TeamDto> updateTeam(Long id, TeamDto compFam);

    void deleteById(Long id);

    List<TeamDto> getAll();
}
