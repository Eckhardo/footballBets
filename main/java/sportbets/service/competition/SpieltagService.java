package sportbets.service.competition;

import sportbets.persistence.entity.competition.Spieltag;
import sportbets.web.dto.competition.SpieltagDto;

import java.util.List;
import java.util.Optional;


public interface SpieltagService {
    Optional<Spieltag> findById(Long id);

    Spieltag save(SpieltagDto spieltagDto);

    Optional<Spieltag> updateMatchDay(Long id, SpieltagDto spieltagDto);

    void deleteById(Long id);

    List<Spieltag> getAll();

    List<Spieltag> getAllForCompetition(Long id);

    Optional<Spieltag> findByNumberAndRound(int spieltagNumber, Long id);
}
