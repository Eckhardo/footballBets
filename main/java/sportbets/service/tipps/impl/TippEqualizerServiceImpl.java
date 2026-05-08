package sportbets.service.tipps.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.tipps.Tipp;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.repository.competition.SpielRepository;
import sportbets.persistence.repository.competition.SpieltagRepository;
import sportbets.persistence.repository.tipps.TippRepository;
import sportbets.service.tipps.TippEqualizerService;

import java.util.List;
import java.util.Set;

@Service
public class TippEqualizerServiceImpl implements TippEqualizerService {

    private static final Logger log = LoggerFactory.getLogger(TippEqualizerServiceImpl.class);

    private final TippRepository tippRepo;
    private final SpielRepository spielRepo;
    private final SpieltagRepository spieltagRepo;

    public TippEqualizerServiceImpl(TippRepository tippRepository, SpielRepository spielRepository, SpieltagRepository spieltagRepository) {
        this.tippRepo = tippRepository;
        this.spielRepo = spielRepository;
        this.spieltagRepo = spieltagRepository;
    }

    @Override
    @Transactional
    public void equalizeTippsForMatchday(Long spieltagId) {
        List<Spiel> spiele = spielRepo.findAllForMatchday(spieltagId);
        for (Spiel spiel : spiele) {
            Set<Tipp> tipps = spiel.getTipps();
            ;
            for (Tipp tipp : tipps) {
                TippModus tippModus = tipp.getTippModus();
                 CommunityMembership cm= tipp.getCommunityMembership();
                int winPoints= tippModus.calculateWinPoints(tipp,spiel);
                log.info("######## winPoints: {}", winPoints);
                tipp.setWinPoints(winPoints);
                tippRepo.save(tipp);
            }
        }

    }

    @Override
    @Transactional
    public void equalizeTippsForCompetition(Long competitionId) {

    }
}
