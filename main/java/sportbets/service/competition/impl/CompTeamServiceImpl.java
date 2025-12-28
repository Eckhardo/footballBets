package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionTeam;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.persistence.repository.competition.CompetitionTeamRepository;
import sportbets.persistence.repository.competition.TeamRepository;
import sportbets.service.competition.CompTeamService;
import sportbets.web.dto.competition.CompetitionTeamDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompTeamServiceImpl implements CompTeamService {

    private static final Logger log = LoggerFactory.getLogger(CompTeamServiceImpl.class);
    private final CompetitionTeamRepository compTeamRepo;
    private final CompetitionRepository compRepo;
    private final TeamRepository teamRepo;
   private final  ModelMapper modelMapper;
    public CompTeamServiceImpl(CompetitionTeamRepository compTeamRepo, CompetitionRepository compRepo, TeamRepository teamRepo, ModelMapper modelMapper) {
        this.compTeamRepo = compTeamRepo;

        this.compRepo = compRepo;
        this.teamRepo = teamRepo;
        this.modelMapper = modelMapper;
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Optional<CompetitionTeam> findById(Long id) {
        return compTeamRepo.findById(id);

    }

    /**
     * @param compTeamDto
     * @return
     */
    @Override
    @Transactional
    public CompetitionTeam save(CompetitionTeamDto compTeamDto) {
        log.info("Service save compTeam {}", compTeamDto);
        Optional<CompetitionTeam> entity = compTeamRepo.findByTeamIdAndCompId(compTeamDto.getTeamId(), compTeamDto.getCompId());

        if (entity.isPresent()) {
            log.error("CompetitionTeam already exists");
            throw new EntityExistsException("CompTeam  already exist with given id:" + compTeamDto.getId());
        }
        CompetitionTeam model = modelMapper.map(compTeamDto, CompetitionTeam.class);
        Competition comp = compRepo.findByName(compTeamDto.getCompName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Team team = teamRepo.findById(compTeamDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.setCompetition(comp);
        model.setTeam(team);

        log.info("save compTeam {}", model);
        return compTeamRepo.save(model);


    }


    /**
     * @param compTeamDtos
     * @return
     */
    @Override
    @Transactional
    public List<CompetitionTeam> saveAll(List<CompetitionTeamDto> compTeamDtos) {

        List<CompetitionTeam> savedTeams = new ArrayList<>();
        for (CompetitionTeamDto compTeamDto : compTeamDtos) {
            Optional<CompetitionTeam> entity = compTeamRepo.findByTeamIdAndCompId(compTeamDto.getTeamId(), compTeamDto.getCompId());

            if (entity.isPresent()) {
                log.error("CompetitionTeam already exists");
                throw new EntityExistsException("CompTeam  already exist with given id:" + compTeamDto.getId());
            }
            CompetitionTeam model = modelMapper.map(compTeamDto, CompetitionTeam.class);
            Competition comp = compRepo.findByName(compTeamDto.getCompName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            Team team = teamRepo.findById(compTeamDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            model.setCompetition(comp);
            model.setTeam(team);
            savedTeams.add(compTeamRepo.save(model));
        }
        return savedTeams;
    }

    /**
     * @param id
     * @param competitionTeamDto
     * @return
     */
    @Override
    @Transactional
    public Optional<CompetitionTeam> update(Long id, CompetitionTeamDto competitionTeamDto) {

        log.info("update Match dto:: {}", competitionTeamDto);
        Optional<CompetitionTeam> updateModel = compTeamRepo.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityExistsException("CompTeam does not exist with given id:" + competitionTeamDto.getId());
        }
        CompetitionTeam model = modelMapper.map(competitionTeamDto, CompetitionTeam.class);
        Competition comp = compRepo.findByName(competitionTeamDto.getCompName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Team team = teamRepo.findById(competitionTeamDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.setCompetition(comp);
        model.setTeam(team);
        CompetitionTeam competitionTeam = updateFields(updateModel.get(), model);
        log.info("updated Comp  with {}", competitionTeam);
        return Optional.of(compTeamRepo.save(competitionTeam));


    }

    /**
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public void deleteAll(List<Long> ids) {
        for (Long id : ids) {
            compTeamRepo.deleteById(id);
        }
    }

    private CompetitionTeam updateFields(CompetitionTeam base, CompetitionTeam compTeam) {
        base.setTeam(compTeam.getTeam());
        base.setCompetition(compTeam.getCompetition());

        return base;
    }

    /**
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        compTeamRepo.deleteById(id);
    }

    /**
     * @param compId
     * @return
     */
    @Override
    @Transactional
    public List<CompetitionTeam> getAllFormComp(Long compId) {


        return compTeamRepo.getAllFormComp(compId);

    }
}
