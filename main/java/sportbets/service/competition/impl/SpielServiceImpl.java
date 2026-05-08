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
    public List<Spiel> saveBatch(MatchBatchRecord matchBatchRecord) {
        log.debug("saveAll :: {}", matchBatchRecord);
        Long compRoundId = matchBatchRecord.compRoundId();

        Competition comp = competitionRepo.findByRoundId(compRoundId).orElseThrow(() -> new EntityNotFoundException("Competition not found"));
        CompetitionRound compRound = competitionRoundRepo.findById(compRoundId).orElseThrow(() -> new EntityNotFoundException("CompetitionRound not found"));
        Long matchdaysSize = spieltagRepo.countByRoundId(compRoundId);
        int numberOfMatches = compRound.getTeamsSize() / 2;
        int numberOfAllowedMatchdays = compRound.getMatchdaysSize();
        int firstMatchday = compRound.getFirstMatchday();
        log.debug("compRound.getTeamsSize() :: {}", compRound.getTeamsSize());
        log.debug("matchdaysSize :: {}", matchdaysSize);
        log.debug("numberOfMatches :: {}", numberOfMatches);
        log.debug("numberOfAllowedMatchdays :: {}", numberOfAllowedMatchdays);
        // assert  size of matchdays is equal to size allowed matchdays (aka all matchdays have to be present)
        if (matchdaysSize != numberOfAllowedMatchdays) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of allowed matchdays is not eqaul to number of existing matchdays ");
        }

        Team heimTeam = retrieveTeam(matchBatchRecord.heimTeamId());
        Team gastTeam = retrieveTeam(matchBatchRecord.gastTeamId());
        List<Spiel> spiele = new ArrayList<>();
        for (int i = 0; i < numberOfAllowedMatchdays; i++) {
            Spieltag matchday = spieltagRepo.findByNumberAndRound(firstMatchday, compRoundId).orElseThrow(() -> new EntityNotFoundException("Spieltag not found"));

            for (int k = 1; k <= numberOfMatches; k++) {
                Spiel spiel = new Spiel(matchday, k, LocalDateTime.now(), heimTeam, gastTeam, 0, 0, false);

                SpielFormula heimFormel = new SpielFormula(spiel, heimTeam.getName(), heimTeam.getAcronym(),
                        true, spiel.getHeimTore(), spiel
                        .getGastTore(), 0);
                heimFormel.calcWinPoints(spiel.isStattgefunden(), comp.getWinMultiplicator(), comp.getRemisMultiplicator());
                heimFormel.calcTrend(heimFormel.getHeimTore(), heimFormel.getGastTore(), spiel.isStattgefunden());

                //  spielFormulaRepo.save(heimFormel);

                SpielFormula gastFormel = new SpielFormula(spiel, gastTeam.getName(), gastTeam.getAcronym(),
                        false, spiel.getGastTore(), spiel
                        .getHeimTore(), 0);
                gastFormel.calcWinPoints(spiel.isStattgefunden(), comp.getWinMultiplicator(), comp.getRemisMultiplicator());
                gastFormel.calcTrend(gastFormel.getHeimTore(), gastFormel.getGastTore(), spiel.isStattgefunden());

                spiele.add(spiel);
            }
            firstMatchday++;
        }

        List<Spiel> saved = spielRepo.saveAll(spiele);
        log.debug("savedSize :: {}", saved.size());
        return saved;
    }

    @Override
    @Transactional
    public Spiel save(SpielDto spielDto) {
        log.debug("save SpielDto :: {}", spielDto);
        Optional<Spiel> optionalSpiel = spielRepo.findByNumberWithSpieltagId(spielDto.getSpielNumber(), spielDto.getSpieltagId());
        if (optionalSpiel.isPresent()) {
            throw new EntityExistsException("Spiel  already exist with given spiel number:" + spielDto.getSpielNumber() + "for spieltag " + spielDto.getSpieltagId());
        }
        Competition comp = competitionRepo.findBySpieltagId(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Competition not found"));
        Spieltag spieltag = spieltagRepo.findById(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Matchday not found"));
        Team heimTeam = retrieveTeam(spielDto.getHeimTeamId());
        Team gastTeam = retrieveTeam(spielDto.getGastTeamId());
        Spiel model = modelMapper.map(spielDto, Spiel.class);
        log.debug("model Spiel :: {}", model);
        model.setSpieltag(spieltag);
        model.setHeimTeam(heimTeam);
        model.setGastTeam(gastTeam);


        SpielFormula heimFormel = new SpielFormula(model, heimTeam.getName(), heimTeam.getAcronym(),
                true, model.getHeimTore(), model
                .getGastTore(), 0);
        heimFormel.calcWinPoints(model.isStattgefunden(), comp.getWinMultiplicator(), comp.getRemisMultiplicator());
        heimFormel.calcTrend(heimFormel.getHeimTore(), heimFormel.getGastTore(), model.isStattgefunden());

        //  spielFormulaRepo.save(heimFormel);

        SpielFormula gastFormel = new SpielFormula(model, gastTeam.getName(), gastTeam.getAcronym(),
                false, model.getGastTore(), model
                .getHeimTore(), 0);
        gastFormel.calcWinPoints(model.isStattgefunden(), comp.getWinMultiplicator(), comp.getRemisMultiplicator());
        gastFormel.calcTrend(gastFormel.getHeimTore(), gastFormel.getGastTore(), model.isStattgefunden());

        log.debug("finally save Spiel :: {}", model);
        return spielRepo.save(model);

    }

    @Override
    @Transactional
    public List<Spiel> saveList(Long spieltagId, List<SpielDto> dtos) {

        log.debug("saveForSpieltag :: {}", dtos);
        Spieltag spieltag = spieltagRepo.findById(spieltagId).orElseThrow(() -> new EntityNotFoundException("Matchday not found"));
        Competition comp = competitionRepo.findBySpieltagId(spieltagId).orElseThrow(() -> new EntityNotFoundException("Competition not found"));

        List<Spiel> toSaveList = new ArrayList<>();
        for (SpielDto spielDto : dtos) {
            Optional<Spiel> optionalSpiel = spielRepo.findByNumberWithSpieltagId(spielDto.getSpielNumber(), spielDto.getSpieltagId());
            if (optionalSpiel.isPresent()) {
                throw new EntityExistsException("Spiel  already exist with given spiel number:" + spielDto.getSpielNumber() + "for spieltag " + spielDto.getSpieltagId());
            }
            Team heimTeam = retrieveTeam(spielDto.getHeimTeamId());
            Team gastTeam = retrieveTeam(spielDto.getGastTeamId());
            Spiel model = modelMapper.map(spielDto, Spiel.class);
            log.debug("model Spiel :: {}", model);
            model.setSpieltag(spieltag);
            model.setHeimTeam(heimTeam);
            model.setGastTeam(gastTeam);

             SpielFormula heimFormel = new SpielFormula(model, heimTeam.getName(), heimTeam.getAcronym(),
                    true, model.getHeimTore(), model
                    .getGastTore(), 0);
            heimFormel.calcWinPoints(model.isStattgefunden(), comp.getWinMultiplicator(), comp.getRemisMultiplicator());
            heimFormel.calcTrend(heimFormel.getHeimTore(), heimFormel.getGastTore(), model.isStattgefunden());

            //  spielFormulaRepo.save(heimFormel);

            SpielFormula gastFormel = new SpielFormula(model, gastTeam.getName(), gastTeam.getAcronym(),
                    false, model.getGastTore(), model
                    .getHeimTore(), 0);
            gastFormel.calcWinPoints(model.isStattgefunden(), comp.getWinMultiplicator(), comp.getRemisMultiplicator());
            gastFormel.calcTrend(gastFormel.getHeimTore(), gastFormel.getGastTore(), model.isStattgefunden());

            toSaveList.add(model);
        }
        return spielRepo.saveAll(toSaveList);
    }

    @Override
    @Transactional
    public Optional<Spiel> updateOne(Long id, SpielDto spielDto) {

        log.debug("update Match dto:: {}", spielDto);
        Spiel savedSpiel = spielRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("spiel  does not exist given id:" + spielDto.getId()));

        Competition savedComp = competitionRepo.findBySpieltagId(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Competition not found"));

        Spieltag spieltag = spieltagRepo.findById(spielDto.getSpieltagId()).orElseThrow(() -> new EntityNotFoundException("Matchday not found"));
        Team heimTeam = retrieveTeam(spielDto.getHeimTeamId());
        Team gastTeam = retrieveTeam(spielDto.getGastTeamId());
        Spiel model = modelMapper.map(spielDto, Spiel.class);
        model.setSpieltag(spieltag);
        model.setHeimTeam(heimTeam);
        model.setGastTeam(gastTeam);

        Spiel updated = updateFields(savedSpiel, model);
        log.debug("updated Match dto:: {}", updated);


        SpielFormula heim = savedSpiel.getSpielFormulaForHeim().orElseThrow();
        heim.setHeimTore(updated.getHeimTore());
        heim.setGastTore(updated.getGastTore());
        heim.calcWinPoints(updated.isStattgefunden(), savedComp.getWinMultiplicator(), savedComp.getRemisMultiplicator());
        heim.calcTrend(heim.getHeimTore(), heim.getGastTore(), updated.isStattgefunden());
        log.debug("heim formula:: {}", heim);
        model.addSpielFormula(heim);


        SpielFormula gast = savedSpiel.getSpielFormulaForGast().orElseThrow();
        gast.setHeimTore(updated.getGastTore());
        gast.setGastTore(updated.getHeimTore());
        gast.calcWinPoints(updated.isStattgefunden(), savedComp.getWinMultiplicator(), savedComp.getRemisMultiplicator());
        gast.calcTrend(gast.getHeimTore(), gast.getGastTore(), updated.isStattgefunden());
        log.debug("gast formula:: {}", gast);
        model.addSpielFormula(gast);

        log.debug("update Spiel  with {}", updated);
        Spiel updatedSpiel = spielRepo.save(model);
        return Optional.of(updatedSpiel);

    }

    @Override
    @Transactional
    public List<Spiel> updateList(Long spieltagId, List<SpielDto> spielDtos) {
        log.debug("updateForSpieltag");

        List<Spiel> spieleToSave = new ArrayList<>();

        Competition savedComp = competitionRepo.findBySpieltagId(spieltagId).orElseThrow(() -> new EntityNotFoundException("Competition not found"));
        Spieltag spieltag = spieltagRepo.findById(spieltagId).orElseThrow(() -> new EntityNotFoundException("Matchday not found"));
        for (SpielDto spielDto : spielDtos) {

            log.debug("update Match dto:: {}", spielDto);
            Spiel savedSpiel = spielRepo.findById(spielDto.getId()).orElseThrow(() -> new EntityNotFoundException("spiel  does not exist given id:" + spielDto.getId()));

            Team heimTeam = retrieveTeam(spielDto.getHeimTeamId());
            Team gastTeam = retrieveTeam(spielDto.getGastTeamId());
            Spiel model = modelMapper.map(spielDto, Spiel.class);
            model.setSpieltag(spieltag);
            model.setHeimTeam(heimTeam);
            model.setGastTeam(gastTeam);

            Spiel updated = updateFields(savedSpiel, model);
            log.debug("updated Match dto:: {}", updated);


            SpielFormula heim = savedSpiel.getSpielFormulaForHeim().orElseThrow();
            heim.setHeimTore(updated.getHeimTore());
            heim.setGastTore(updated.getGastTore());
            heim.calcWinPoints(updated.isStattgefunden(), savedComp.getWinMultiplicator(), savedComp.getRemisMultiplicator());
            heim.calcTrend(heim.getHeimTore(), heim.getGastTore(), updated.isStattgefunden());

            log.debug("heim formula:: {}", heim);
            model.addSpielFormula(heim);


            SpielFormula gast = savedSpiel.getSpielFormulaForGast().orElseThrow();

            gast.setHeimTore(updated.getGastTore());
            gast.setGastTore(updated.getHeimTore());
            gast.calcWinPoints(updated.isStattgefunden(), savedComp.getWinMultiplicator(), savedComp.getRemisMultiplicator());
            gast.calcTrend(gast.getHeimTore(), gast.getGastTore(), updated.isStattgefunden());
            log.debug("gast formula:: {}", gast);
            model.addSpielFormula(gast);
            spieleToSave.add(model);
        }
        return spielRepo.saveAll(spieleToSave);
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
    @Transactional
    public void deleteById(Long id) {
        spielRepo.deleteById(id);
    }


    @Override

    public List<Spiel> getAll() {
        return spielRepo.findAll();

    }

    @Transactional
    public Team retrieveTeam(Long id) {
        return teamRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Team heim not found"));
    }
}
