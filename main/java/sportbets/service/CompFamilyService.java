package sportbets.service;

import org.modelmapper.ModelMapper;
import sportbets.persistence.entity.Team;
import sportbets.web.dto.CompetitionFamilyDto;

import java.util.List;
import java.util.Optional;

public interface CompFamilyService {

    ModelMapper getModelMapperForFamily();

    Optional<CompetitionFamilyDto> findById(Long id);

    Optional<CompetitionFamilyDto> save(CompetitionFamilyDto compFam);

    Optional<CompetitionFamilyDto> updateFamily(Long id, CompetitionFamilyDto compFam);

    List<Team> findTeams(Long id);
    void deleteByName(String name );

    void deleteById(Long id);
    List<CompetitionFamilyDto> getAll();

}