package sportbets.service.tipps;

import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.tipps.TippConfig;
import sportbets.persistence.rowObject.TippConfigRow;
import sportbets.web.dto.competition.SpielDto;
import sportbets.web.dto.competition.batch.MatchBatchRecord;
import sportbets.web.dto.tipps.TippConfigDto;

import java.util.List;
import java.util.Optional;

public interface TippConfigService {


    Optional<TippConfigDto> findById(Long id);

    TippConfigDto save(TippConfigDto dto);

    void deleteById(Long id);

    List<TippConfigRow> findAllForRound( Long compRoundId,Long compMembId);

    List<TippConfigRow> findTippConfigRows(Long compMembId);

  Optional<TippConfigRow> findTippConfig(Long spieltagId, Long compMembId);
}
