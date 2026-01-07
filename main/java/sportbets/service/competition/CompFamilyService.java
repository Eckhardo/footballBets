package sportbets.service.competition;

import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.web.dto.competition.CompetitionFamilyDto;


import java.util.List;
import java.util.Optional;

public interface CompFamilyService {


    Optional<CompetitionFamily> findById(Long id);

    Optional<CompetitionFamily> save(CompetitionFamilyDto compFamDto);

    Optional<CompetitionFamily> updateFamily(Long id, CompetitionFamilyDto compFamDto);

    void deleteByName(String name);

    void deleteById(Long id);

    List<CompetitionFamily> getAll();

    Optional<CompetitionFamily> findByIdTest(Long familyId);

    Optional<CompetitionFamily> findByByName(String name);
}