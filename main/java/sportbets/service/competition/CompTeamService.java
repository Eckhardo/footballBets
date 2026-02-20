package sportbets.service.competition;

import sportbets.persistence.entity.competition.CompetitionTeam;
import sportbets.persistence.entity.competition.Team;
import sportbets.web.dto.competition.CompetitionTeamDto;

import java.util.List;
import java.util.Optional;

public interface CompTeamService {
    Optional<CompetitionTeam> findById(Long id);

    CompetitionTeam save(CompetitionTeamDto compTeamDto);

    List<CompetitionTeam> saveAll(List<CompetitionTeamDto> compTeamDtos);

    Optional<CompetitionTeam> update(Long id, CompetitionTeamDto competitionTeamDto);

    void deleteAll(List<Long> ids);

    void deleteById(Long id);

    List<CompetitionTeam> getAllForComp(Long compId);
    List<Team>  findUnregisteredTeams(boolean isClub, List<CompetitionTeam> models);
    Optional<CompetitionTeam> findByTeamIdAndCompId(Long teamId, Long compId);
}
