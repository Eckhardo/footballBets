package sportbets.service;

import sportbets.persistence.entity.Competition;

import java.util.Optional;

public interface CompService {

    Optional<Competition> findById(Long id);

    Competition save(Competition compFam);

    Optional<Competition> updateComp(Long id, Competition compFam);

}
