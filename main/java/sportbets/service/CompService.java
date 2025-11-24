package sportbets.service;

import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionFamily;

import java.util.List;
import java.util.Optional;

public interface CompService {

    Optional<Competition> findById(Long id);

    Competition save(Competition compFam);

    Optional<Competition> updateComp(Long id, Competition compFam);

    void deleteById(Long id);
    List<Competition> getAll();

}
