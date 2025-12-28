package sportbets.service.competition;

import sportbets.persistence.entity.competition.CompetitionTeam;
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

    List<CompetitionTeam> getAllFormComp(Long compId);

}
