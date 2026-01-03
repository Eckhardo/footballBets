package sportbets.service.competition;

import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.web.dto.competition.CompetitionMembershipDto;

import java.util.List;
import java.util.Optional;

public interface CompetitionMembershipService {
    Optional<CompetitionMembership> findById(Long id);

    CompetitionMembership save(CompetitionMembershipDto compMembDto);

    Optional<CompetitionMembership> update(Long id, CompetitionMembershipDto compMembDto);

    void deleteById(Long id);
    List<CompetitionMembership> getAll();
}
