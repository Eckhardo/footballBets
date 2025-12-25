package sportbets.service.competition;

import sportbets.web.dto.competition.SpieltagDto;

import java.util.List;
import java.util.Optional;


public interface SpieltagService {
    Optional<SpieltagDto> findById(Long id);

    Optional<SpieltagDto> save(SpieltagDto spieltag);

    Optional<SpieltagDto> updateMatchDay(Long id, SpieltagDto spieltag);

    void deleteById(Long id);

    List<SpieltagDto> getAllForRound(Long id);

    List<SpieltagDto> getAll();

}
