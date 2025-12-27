package sportbets.service.competition;

import sportbets.persistence.entity.competition.CompetitionRound;

import java.util.List;
import java.util.Optional;

public interface CompRoundService {
    Optional<CompetitionRound> findById(Long id);

    CompetitionRound save(CompetitionRound compRound);

    Optional<CompetitionRound> updateRound(Long id, CompetitionRound compRound);

    void deleteById(Long id);

    void deleteByName(String name);

    List<CompetitionRound> getAll();
}
