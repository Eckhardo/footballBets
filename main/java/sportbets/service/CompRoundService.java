package sportbets.service;

import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionRound;
import sportbets.web.dto.CompetitionDto;
import sportbets.web.dto.CompetitionRoundDto;

import java.util.List;
import java.util.Optional;
@Transactional
public interface CompRoundService {
    Optional<CompetitionRoundDto> findById(Long id);

    Optional<CompetitionRoundDto> save(CompetitionRoundDto compFam);

    Optional<CompetitionRoundDto> updateRound(Long id, CompetitionRoundDto compFam);

    void deleteById(Long id);

    List<CompetitionRound> getAll();

}
