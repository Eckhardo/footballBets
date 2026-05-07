package sportbets.service.competition;

import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.competition.Team;
import sportbets.web.dto.competition.SpielDto;
import sportbets.web.dto.competition.batch.MatchBatchRecord;

import java.util.List;
import java.util.Optional;

public interface SpielService {
    Optional<Spiel> findById(Long id);

    Spiel save(SpielDto spiel);
    List<Spiel> saveForSpieltag(Long spieltagId, List<SpielDto> spiele);

    Optional<Spiel> updateSpiel(Long id, SpielDto spiel);
    List<Spiel> updateForSpieltag(Long id, List<SpielDto> spielDto);

    void deleteById(Long id);

    List<Spiel> getAllForMatchday(Long id);

    List<Spiel> getAll();
    List<Spiel> saveAll(MatchBatchRecord matchBatchRecord);
    Team retrieveTeam(Long id);

}
