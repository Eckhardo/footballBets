package sportbets.service.competition;

import sportbets.persistence.entity.competition.Spiel;
import sportbets.web.dto.competition.SpielDto;

import java.util.List;
import java.util.Optional;

public interface SpielService {
    Optional<Spiel> findById(Long id);

    Spiel save(SpielDto spiel);

    Optional<Spiel> updateSpiel(Long id, SpielDto spiel);

    void deleteById(Long id);

    List<Spiel> getAllForMatchday(Long id);

    List<Spiel> getAll();
}
