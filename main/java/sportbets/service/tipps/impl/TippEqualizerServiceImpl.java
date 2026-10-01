package sportbets.service.tipps.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.tipps.Tipp;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.repository.competition.SpielRepository;
import sportbets.persistence.repository.tipps.TippRepository;
import sportbets.service.tipps.TippEqualizerService;

import java.util.List;
import java.util.Set;

@Service
public class TippEqualizerServiceImpl implements TippEqualizerService {

    private static final Logger log = LoggerFactory.getLogger(TippEqualizerServiceImpl.class);

    private final TippRepository tippRepo;
    private final SpielRepository spielRepo;


    public TippEqualizerServiceImpl(TippRepository tippRepository, SpielRepository spielRepo) {
        this.tippRepo = tippRepository;
        this.spielRepo = spielRepo;
    
    }

    @Override
    @Transactional
    public void equalizeTippsForMatchday(Long spieltagId) {
        List<Spiel> spiele = spielRepo.findAllForMatchday(spieltagId);
        for (Spiel spiel : spiele) {
            if (spiel.isStattgefunden()) {
                Set<Tipp> tipps = spiel.getTipps();
                for (Tipp tipp : tipps) {
                    TippModus tippModus = tipp.getTippModus();
                    int winPoints = tippModus.calculateWinPoints(tipp, spiel);
                    tipp.setWinPoints(winPoints);
                    tippRepo.save(tipp);
                }
            }
        }

    }

    @Override
    @Transactional
    public void equalizeTippsForCompetition(Long competitionId) {

    }
}
