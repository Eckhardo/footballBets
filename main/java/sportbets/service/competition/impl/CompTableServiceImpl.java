package sportbets.service.competition.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.repository.competition.CompTableRepository;
import sportbets.persistence.rowObject.TeamPositionSummaryRow;
import sportbets.service.competition.CompTableService;
import sportbets.web.dto.competition.search.TableSearchCriteria;

import java.util.List;

@Service
public class CompTableServiceImpl implements CompTableService {

    private static final Logger log = LoggerFactory.getLogger(CompTableServiceImpl.class);
    private final CompTableRepository compTableRepository;

    public CompTableServiceImpl(CompTableRepository compTableRepository) {
        this.compTableRepository = compTableRepository;
    }

    @Override
    @Transactional
    public List<TeamPositionSummaryRow> findTableForLigaModus(TableSearchCriteria searchCriteria) {
        List<TeamPositionSummaryRow> rows= compTableRepository.findTableForLigaModus(searchCriteria.getCompId(), searchCriteria.getStartSpieltag(), searchCriteria.getEndSpieltag());
        log.info("rows size:"+rows.size());

        return rows;
    }

    @Override
    @Transactional
    public List<TeamPositionSummaryRow> findTableHeimOrGastForLigaModus(TableSearchCriteria searchCriteria) {
        List<TeamPositionSummaryRow> rows= compTableRepository.findTableHeimOrGastForLigaModus(searchCriteria.getCompId(), searchCriteria.getStartSpieltag(), searchCriteria.getEndSpieltag(), searchCriteria.getHeimOrGast());
        log.info("rows size:"+rows.size());
        return rows;
    }

    @Override
    @Transactional
    public List<TeamPositionSummaryRow> findTableHeimOrGastForLigaModus2(Long compId, boolean isHeimGast, int spieltagStart, int spieltagEnd) {
      List<TeamPositionSummaryRow> rows=compTableRepository.findTableHeimOrGastForLigaModus2(compId, isHeimGast,spieltagStart, spieltagEnd);
       log.info("rows size:"+rows.size());
      return rows;
    }

}
