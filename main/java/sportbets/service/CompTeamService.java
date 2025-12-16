package sportbets.service;

import sportbets.web.dto.CompetitionTeamDto;

import java.util.List;
import java.util.Optional;

public interface CompTeamService {
    Optional<CompetitionTeamDto> findById(Long id);

   Optional<CompetitionTeamDto> save(CompetitionTeamDto dto);

    List<CompetitionTeamDto> saveAll(List<CompetitionTeamDto> dtos);

    Optional<CompetitionTeamDto> update(Long id, CompetitionTeamDto dto);
    void deleteAll(List<CompetitionTeamDto> dtos);

    void deleteById(Long id);

    List<CompetitionTeamDto> getAllFormComp(Long compId);

}
