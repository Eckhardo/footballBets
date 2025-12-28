package sportbets.service.competition;

import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.web.dto.competition.CompetitionRoundDto;

import java.util.List;
import java.util.Optional;

public interface CompRoundService {
    Optional<CompetitionRound> findById(Long id);

    CompetitionRound save(CompetitionRoundDto compRoundDto);

    Optional<CompetitionRound> updateRound(Long id, CompetitionRoundDto compRoundDto);

    void deleteById(Long id);

    void deleteByName(String name);

}
