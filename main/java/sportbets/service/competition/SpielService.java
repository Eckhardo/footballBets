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
    List<Spiel> saveList(Long spieltagId, List<SpielDto> spiele);

    Optional<Spiel> updateOne(Long id, SpielDto spiel);
    List<Spiel> updateList(Long id, List<SpielDto> spielDto);

    void deleteById(Long id);

    List<Spiel> getAllForMatchday(Long id);

    List<Spiel> getAll();
    List<Spiel> saveBatch(MatchBatchRecord matchBatchRecord);
    Team retrieveTeam(Long id);

}
