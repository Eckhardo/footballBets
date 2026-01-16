package sportbets.service.competition;

import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.TeamDto;

import java.util.List;
import java.util.Optional;

public interface CompService {

    Optional<Competition> findById(Long id);

    Optional<Competition> findByName(String name);

    Competition save(CompetitionDto compDto);

    Optional<Competition> updateComp(Long id, CompetitionDto compDto);

    void deleteById(Long id);

    void deleteByName(String name);

    List<Competition> getAll();

    Optional<Competition> findByNameJoinFetchRounds(String name);

    List<TeamDto> findTeamsForComp(Long compId);

    Competition findByIdJoinFetchRounds(Long id);

    List<CompetitionRound> getAllFormComp(Long compId);

    Optional<Competition> findByIdTest(Long id);

    List<Competition> findByFamilyId(Long familyId);
}
