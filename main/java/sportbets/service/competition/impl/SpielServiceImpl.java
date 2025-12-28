package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.SpielRepository;
import sportbets.persistence.repository.competition.SpieltagRepository;
import sportbets.persistence.repository.competition.TeamRepository;
import sportbets.service.competition.SpielService;
import sportbets.web.dto.MapperUtil;
import sportbets.web.dto.competition.SpielDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpielServiceImpl implements SpielService {
    private static final Logger log = LoggerFactory.getLogger(SpielServiceImpl.class);
    private final SpielRepository spielRepo;
    private final SpieltagRepository spieltagRepo;
    private final TeamRepository teamRepo;

    public SpielServiceImpl(SpielRepository spielRepo, SpieltagRepository spieltagRepo, TeamRepository teamRepo) {
        this.spielRepo = spielRepo;
        this.spieltagRepo = spieltagRepo;
        this.teamRepo = teamRepo;
    }

    @Override
    @Transactional
    public List<Spiel> getAllForMatchday(Long id) {

    return spielRepo.findAllForMatchday(id);

    }

    @Override
    public Optional<Spiel> findById(Long id) {
       return spielRepo.findById(id);

    }

    @Override
    @Transactional
    public Spiel save(Spiel spiel) {
        log.info("save Spiel :: {}", spiel);
        Optional<Spiel> optionalSpiel = spielRepo.findByNumberWithSpieltagId(spiel.getSpielNumber(), spiel.getSpieltag().getId());
        if (optionalSpiel.isPresent()) {
            throw new EntityExistsException("Spiel  already exist with given id:" + spiel.getId());
        }
        return spielRepo.save(spiel);



    }

    @Override
    @Transactional
    public Optional<Spiel> updateSpiel(Long id, Spiel spiel) {

        log.info("update Match dto:: {}", spiel);
        Optional<Spiel> updateModel = spielRepo.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityExistsException("spiel  already exist with given id:" + spiel.getId());
        }


        Spiel updated = updateFields(updateModel.get(), spiel);
        log.info("updated Comp  with {}", updated);
        return Optional.of( spielRepo.save(updated));

    }

    private Spiel updateFields(Spiel base, Spiel updatedMatch) {
        base.setAnpfiffdate(updatedMatch.getAnpfiffdate());
        base.setSpielNumber(updatedMatch.getSpielNumber());
        base.setHeimTore(updatedMatch.getHeimTore());
        base.setStattgefunden(updatedMatch.isStattgefunden());
        base.setGastTore(updatedMatch.getGastTore());
        base.setSpieltag(updatedMatch.getSpieltag());
        base.setHeimTeam(updatedMatch.getHeimTeam());
        base.setGastTeam(updatedMatch.getGastTeam());
        return base;
    }

    @Override
    public void deleteById(Long id) {
        spielRepo.deleteById(id);
    }


    @Override
    public List<Spiel> getAll() {
      return spielRepo.findAll();

    }
}
