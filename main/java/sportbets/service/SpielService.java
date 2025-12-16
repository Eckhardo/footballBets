package sportbets.service;

import sportbets.web.dto.SpielDto;

import java.util.List;
import java.util.Optional;

public interface SpielService {
    Optional<SpielDto> findById(Long id);

    Optional<SpielDto> save(SpielDto spiel);

    Optional<SpielDto> updateSpiel(Long id, SpielDto spiel);

    void deleteById(Long id);



    List<SpielDto> getAll();
}
