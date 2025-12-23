package sportbets.service;

import sportbets.persistence.entity.competition.Competition;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionRoundDto;
import sportbets.web.dto.competition.TeamDto;

import java.util.List;
import java.util.Optional;

public interface CompService {

    Optional<CompetitionDto> findById(Long id);

    Optional<CompetitionDto> save(CompetitionDto compFam);

    Optional<CompetitionDto> updateComp(Long id, CompetitionDto compFam);

    void deleteById(Long id);
    void deleteByName(String name );
    List<CompetitionDto> getAll();

    Optional<Competition> findByNameJoinFetchRounds(String name);

    List<TeamDto> findTeamsForComp(Long compId);
    Competition findByIdJoinFetchRounds(Long id);
    List<CompetitionRoundDto> getAllFormComp(Long compId);
}
