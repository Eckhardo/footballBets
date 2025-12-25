package sportbets.service.competition;

import sportbets.web.dto.competition.CompetitionRoundDto;

import java.util.List;
import java.util.Optional;

public interface CompRoundService {
    Optional<CompetitionRoundDto> findById(Long id);

    Optional<CompetitionRoundDto> save(CompetitionRoundDto compFam);

    Optional<CompetitionRoundDto> updateRound(Long id, CompetitionRoundDto compFam);

    void deleteById(Long id);

    void deleteByName(String name);

    List<CompetitionRoundDto> getAll();
}
