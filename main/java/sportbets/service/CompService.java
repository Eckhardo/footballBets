package sportbets.service;

import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.Competition;
import sportbets.web.dto.CompetitionDto;

import java.util.List;
import java.util.Optional;
@Transactional
public interface CompService {

    Optional<CompetitionDto> findById(Long id);

    Optional<CompetitionDto> save(CompetitionDto compFam);

    Optional<CompetitionDto> updateComp(Long id, CompetitionDto compFam);

    void deleteById(Long id);

    List<CompetitionDto> getAll();

    Optional<Competition> findByNameJoinFetchRounds(String name);

    Competition findByIdJoinFetchRounds(Long id);
}
