package sportbets.service.competition.impl;

import org.springframework.stereotype.Service;
import sportbets.persistence.repository.competition.SpielFormulaRepository;
import sportbets.persistence.rowObject.TeamPositionSummaryRow;
import sportbets.service.competition.SpielFormulaService;

import java.util.List;

@Service
public class SpielFormulaServiceImpl implements SpielFormulaService {
    private final SpielFormulaRepository spielFormulaRepository;
    public SpielFormulaServiceImpl(SpielFormulaRepository spielFormulaRepository) {
        this.spielFormulaRepository = spielFormulaRepository;
    }

    @Override
    public List<TeamPositionSummaryRow> findSpielFormulaForLigaModus(Long compId, boolean isHeimTeam, long firstSp, Long lastSp) {
        return spielFormulaRepository.findTableForLigaModus(compId, isHeimTeam, firstSp, lastSp);
    }
}
