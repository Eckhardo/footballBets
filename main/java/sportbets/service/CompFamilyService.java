package sportbets.service;

import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.entity.Team;

import java.util.List;
import java.util.Optional;

public interface CompFamilyService {

    Optional<CompetitionFamily> findById(Long id);

    CompetitionFamily save(CompetitionFamily compFam);

    Optional<CompetitionFamily> updateFamily(Long id, CompetitionFamily compFam);

    List<Team> findTeams(Long id);
    void deleteByName(String name );

    void deleteById(Long id);
    List<CompetitionFamily> getAll();

}