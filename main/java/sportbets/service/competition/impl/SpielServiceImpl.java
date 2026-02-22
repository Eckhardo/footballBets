package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.competition.*;
import sportbets.persistence.repository.competition.*;
import sportbets.service.competition.SpielService;
import sportbets.web.dto.competition.SpielDto;
import sportbets.web.dto.competition.batch.MatchBatchRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpielServiceImpl implements SpielService {
    private static final Logger log = LoggerFactory.getLogger(SpielServiceImpl.class);

    private final CompetitionRepository competitionRepo;
    private final CompetitionRoundRepository competitionRoundRepo;
    private final SpielRepository spielRepo;
    private final SpieltagRepository spieltagRepo;
    private final TeamRepository teamRepo;
    private final ModelMapper modelMapper;

    public SpielServiceImpl(CompetitionRepository competitionRepo, CompetitionRoundRepository competitionRoundRepo, SpielRepository spielRepo, SpieltagRepository spieltagRepo, TeamRepository teamRepo, ModelMapper modelMapper) {
        this.competitionRepo = competitionRepo;
        this.competitionRoundRepo = competitionRoundRepo;
        this.spielRepo = spielRepo;
        this.spieltagRepo = spieltagRepo;
        this.teamRepo = teamRepo;
        this.modelMapper = modelMapper;
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
    public List<Spiel> saveAll(MatchBatchRecord matchBatchRecord) {
        log.debug("saveAll :: {}", matchBatchRecord);
        Long compRoundId = matchBatchRecord.compRoundId();

        Competition comp = competitionRepo.findByRoundId(compRoundId).orElseThrow(() -> new EntityNotFoundException("Competition not found"));
        CompetitionRound compRound = competitionRoundRepo.findById(compRoundId).orElseThrow(() -> new EntityNotFoundException("CompetitionRound not found"));
        Long matchdaysSize=spieltagRepo. countByRoundId(compRoundId);
        int numberOfMatches = compRound.getTeamsSize() / 2;
        int numberOfAllowedMatchdays = compRound.getMatchdaysSize();
        int firstMatchday= compRound.getFirstMatchday();


        // assert  size of matchdays is equal to size allowed matchdays (aka all matchdays have to be present)
        if(matchdaysSize!= numberOfAllowedMatchdays){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Number of allowed matchdays is not eqaul to number of existing matchdays ");
        }
        log.info("matchdaysSize :: {}", matchdaysSize);
        Team heimTeam = teamRepo.findById(matchBatchRecord.heimTeamId()).orElseThrow(() -> new EntityNotFoundException("Team heim not found"));
        Team gastTeam = teamRepo.findById(matchBatchRecord.gastTeamId()).orElseThrow(() -> new EntityNotFoundException("Team gast not found"));

        List<Spiel> spiele = new ArrayList<>();
        for (int i = 0; i < numberOfAllowedMatchdays; i++) {
            Spieltag matchday = spieltagRepo.findByNumberAndRound(firstMatchday, compRoundId).orElseThrow(() -> new EntityNotFoundException("Spieltag not found"));

            for (int k = 1; k <= numberOfMatches; k++) {
                Spiel spiel = new Spiel(matchday, k, LocalDateTime.now(), heimTeam, gastTeam, 0, 0, false);

                int heimPoints = SpielFormula.calculatePoints(comp,
                        spiel.getHeimTore(), spiel.getGastTore(), spiel
                                .isStattgefunden());
                SpielFormula heimFormel = new SpielFormula(spiel, heimTeam.getName(), heimTeam.getAcronym(),
                        true, spiel.getHeimTore(), spiel
                        .getGastTore(), heimPoints);

                //  spielFormulaRepo.save(heimFormel);
                int gastPoints = SpielFormula.calculatePoints(comp,
                        spiel.getGastTore(), spiel.getHeimTore(), spiel
                                .isStattgefunden());
                SpielFormula gastFormel = new SpielFormula(spiel, gastTeam.getName(), gastTeam.getAcronym(),
                        false, spiel.getGastTore(), spiel
                        .getHeimTore(), gastPoints);

                spiel.addSpielFormula(heimFormel);
                spiel.addSpielFormula(gastFormel);
                spiele.add(spiel);
            }
            firstMatchday++;
        }

      List<Spiel> saved= spielRepo.saveAll(spiele);
        log.debug("savedSize :: {}", saved.size());
        return saved;
    }

    @Override
    @Transactional
    public Spiel save(SpielDto spielDto) {
        log.info("save SpielDto :: {}", spielDto);
        Optional<Spiel> optionalSpiel = spielRepo.findByNumberWithSpieltagId(spielDto.getSpielNumber(), spielDto.getSpieltagId());
        if (optionalSpiel.isPresent()) {
            throw new EntityExistsException("Spiel  already exist with given spiel number:" + spielDto.getSpielNumber() + "for spieltag " + spielDto.getSpieltagNumber());
        }
        Competition comp = competitionRepo.findBySpieltagId(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Competition not found"));
        Spieltag spieltag = spieltagRepo.findById(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Matchday not found"));
        Team heimTeam = teamRepo.findById(spielDto.getHeimTeamId()).orElseThrow(() -> new EntityNotFoundException("Team heim not found"));
        Team gastTeam = teamRepo.findById(spielDto.getGastTeamId()).orElseThrow(() -> new EntityNotFoundException("Team gast not found"));
        Spiel model = modelMapper.map(spielDto, Spiel.class);
        log.info("model Spiel :: {}", model);
        model.setSpieltag(spieltag);
        model.setHeimTeam(heimTeam);
        model.setGastTeam(gastTeam);

        int heimPoints = SpielFormula.calculatePoints(comp,
                model.getHeimTore(), model.getGastTore(), model
                        .isStattgefunden());
        SpielFormula heimFormel = new SpielFormula(model, heimTeam.getName(), heimTeam.getAcronym(),
                true, model.getHeimTore(), model
                .getGastTore(), heimPoints);

        //  spielFormulaRepo.save(heimFormel);
        int gastPoints = SpielFormula.calculatePoints(comp,
                model.getGastTore(), model.getHeimTore(), model
                        .isStattgefunden());
        SpielFormula gastFormel = new SpielFormula(model, gastTeam.getName(), gastTeam.getAcronym(),
                false, model.getGastTore(), model
                .getHeimTore(), gastPoints);

        model.addSpielFormula(heimFormel);
        model.addSpielFormula(gastFormel);
        log.info("finally save Spiel :: {}", model);
        return spielRepo.save(model);


    }

    @Override
    @Transactional
    public Optional<Spiel> updateSpiel(Long id, SpielDto spielDto) {

        log.info("update Match dto:: {}", spielDto);
        Spiel savedSpiel = spielRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("spiel  does not exist given id:" + spielDto.getId()));
        ;

        Competition savedComp = competitionRepo.findBySpieltagId(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Competition not found"));

        Spieltag spieltag = spieltagRepo.findById(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Matchday not found"));
        Team heimTeam = teamRepo.findById(spielDto.getHeimTeamId()).orElseThrow(() -> new EntityNotFoundException("Team heim not found"));
        Team gastTeam = teamRepo.findById(spielDto.getGastTeamId()).orElseThrow(() -> new EntityNotFoundException("Team gast not found"));
        Spiel model = modelMapper.map(spielDto, Spiel.class);
        model.setSpieltag(spieltag);
        model.setHeimTeam(heimTeam);
        model.setGastTeam(gastTeam);

        Spiel updated = updateFields(savedSpiel, model);
        log.info("updated Match dto:: {}", updated);


        SpielFormula heim = savedSpiel.getSpielFormulaForHeim().orElseThrow();

        int heimPoints = SpielFormula.calculatePoints(savedComp,
                updated.getHeimTore(), updated.getGastTore(), updated
                        .isStattgefunden());

        heim.setPoints(heimPoints);
        heim.setHeimTore(updated.getHeimTore());
        heim.setGastTore(updated.getGastTore());
        heim.calculateTrend(heim.getHeimTore(),
                heim.getGastTore(), updated.isStattgefunden());

        log.info("heim formula:: {}", heim);

        model.addSpielFormula(heim);


        SpielFormula gast = savedSpiel.getSpielFormulaForGast().orElseThrow();

        int gastPoints = SpielFormula.calculatePoints(savedComp,
                updated.getGastTore(), updated.getHeimTore(), updated
                        .isStattgefunden());

        gast.setPoints(gastPoints);

        gast.setHeimTore(updated.getGastTore());
        gast.setGastTore(updated.getHeimTore());
        gast.calculateTrend(gast.getHeimTore(),
                gast.getGastTore(), updated.isStattgefunden());
        model.addSpielFormula(gast);
        log.info("update Soiel  with {}", updated);
        Spiel updatedSpiel = spielRepo.save(model);
        SpielFormula heimel = updatedSpiel.getSpielFormulaForHeim().orElseThrow();
        SpielFormula gastel = updatedSpiel.getSpielFormulaForGast().orElseThrow();
        log.info("finally saved heimel :: {}", heimel);
        log.info("finally saved gastel :: {}", gastel);
        return Optional.of(updatedSpiel);

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
