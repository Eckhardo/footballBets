package sportbets.service.competition;

import sportbets.persistence.entity.competition.Competition;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.TeamDto;

import java.util.List;
import java.util.Optional;

public interface CompService {

    Optional<Competition> findById(Long id);

    Competition save(Competition comp);

    Competition updateComp(Long id, Competition comp);

    void deleteById(Long id);

    void deleteByName(String name);

    List<Competition> getAll();

    Optional<Competition> findByNameJoinFetchRounds(String name);

    List<TeamDto> findTeamsForComp(Long compId);

    Competition findByIdJoinFetchRounds(Long id);

    List<CompetitionRoundDto> getAllFormComp(Long compId);

    Optional<Competition> findByIdTest(Long id);
}
