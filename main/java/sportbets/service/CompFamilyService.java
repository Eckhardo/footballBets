package sportbets.service;

import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.Team;
import sportbets.web.dto.CompetitionFamilyDto;

import java.util.List;
import java.util.Optional;
@Transactional
public interface CompFamilyService {

    Optional<CompetitionFamilyDto> findById(Long id);

    Optional<CompetitionFamilyDto> save(CompetitionFamilyDto compFam);

    Optional<CompetitionFamilyDto> updateFamily(Long id, CompetitionFamilyDto compFam);

    List<Team> findTeams(Long id);
    void deleteByName(String name );

    void deleteById(Long id);
    List<CompetitionFamilyDto> getAll();

}