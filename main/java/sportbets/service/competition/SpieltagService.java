package sportbets.service.competition;

import sportbets.persistence.entity.competition.Spieltag;

import java.util.List;
import java.util.Optional;


public interface SpieltagService {
    Optional<Spieltag> findById(Long id);

    Spieltag save(Spieltag spieltag);

    Optional<Spieltag> updateMatchDay(Long id, Spieltag spieltag);

    void deleteById(Long id);

    List<Spieltag> getAllForRound(Long id);

    List<Spieltag> getAll();

}
