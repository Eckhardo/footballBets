package sportbets.service.competition;

import sportbets.persistence.entity.competition.Spiel;

import java.util.List;
import java.util.Optional;

public interface SpielService {
    Optional<Spiel> findById(Long id);

    Spiel save(Spiel spiel);

    Optional<Spiel> updateSpiel(Long id, Spiel spiel);

    void deleteById(Long id);

    List<Spiel> getAllForMatchday(Long id);

    List<Spiel> getAll();
}
