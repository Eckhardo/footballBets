package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.repository.competition.SpieltagRepository;
import sportbets.service.competition.SpieltagService;

import java.util.List;
import java.util.Optional;

@Service
public class SpieltagServiceImpl implements SpieltagService {

    private static final Logger log = LoggerFactory.getLogger(SpieltagServiceImpl.class);
    private final SpieltagRepository spieltagRepository;

    public SpieltagServiceImpl(SpieltagRepository spieltagRepository) {

        this.spieltagRepository = spieltagRepository;

    }

    @Override
    @Transactional
    public List<Spieltag> getAll() {

        return spieltagRepository.findAll();

    }


    @Override
    public Optional<Spieltag> findById(Long id) {
        return spieltagRepository.findById(id);
    }

    @Override
    @Transactional
    public Spieltag save(Spieltag spieltag) {
        log.info("save :: {}", spieltag);
        Optional<Spieltag> optionalSpieltag = spieltagRepository.findByNumberWithRoundId(spieltag.getSpieltagNumber(), spieltag.getCompetitionRound().getId());
        if (optionalSpieltag.isPresent()) {
            throw new EntityExistsException("Spieltag  already exist with given id:" + spieltag.getId());
        }
        return spieltagRepository.save(spieltag);


    }

    @Override
    @Transactional
    public Optional<Spieltag> updateMatchDay(Long id, Spieltag spieltag) {


        log.info("updateMatchday:: {}", spieltag);
        Optional<Spieltag> updateModel = spieltagRepository.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityExistsException("Spieltag  does not exist with given id:" + spieltag.getId());
        }


        Spieltag model = updateFields(updateModel.get(), spieltag);
        log.info("updated Comp  with {}", model);
        return Optional.of(spieltagRepository.save(model));

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        spieltagRepository.deleteById(id);
    }

    @Override
    public List<Spieltag> getAllForRound(Long roundId) {
        return spieltagRepository.findAllByRoundId(roundId);

    }


    private Spieltag updateFields(Spieltag base, Spieltag updatedMatchDay) {
        base.setSpieltagNumber(updatedMatchDay.getSpieltagNumber());
        base.setStartDate(updatedMatchDay.getStartDate());

        return base;
    }
}
