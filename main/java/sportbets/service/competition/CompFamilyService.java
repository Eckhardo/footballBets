package sportbets.service.competition;

import sportbets.persistence.entity.competition.CompetitionFamily;

import java.util.List;
import java.util.Optional;

public interface CompFamilyService {


    Optional<CompetitionFamily> findById(Long id);

    Optional<CompetitionFamily> save(CompetitionFamily compFam);

    Optional<CompetitionFamily> updateFamily(Long id, CompetitionFamily compFam);

    void deleteByName(String name);

    void deleteById(Long id);

    List<CompetitionFamily> getAll();

    Optional<CompetitionFamily> findByIdTest(Long familyId);
}