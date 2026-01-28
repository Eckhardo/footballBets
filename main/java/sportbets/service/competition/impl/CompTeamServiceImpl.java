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
    private final ModelMapper modelMapper;

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
        log.debug("Service save compTeam {}", compTeamDto);
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

        log.debug("save compTeam {}", model);
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

        log.debug("update Match dto:: {}", competitionTeamDto);
        Optional<CompetitionTeam> updateModel = compTeamRepo.findById(id);
        if (updateModel.isEmpty()) {
            throw new EntityNotFoundException("compTeam  does not exits given id:" + competitionTeamDto.getId());
        }
        CompetitionTeam model = modelMapper.map(competitionTeamDto, CompetitionTeam.class);
        Competition comp = compRepo.findByName(competitionTeamDto.getCompName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Team team = teamRepo.findById(competitionTeamDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.setCompetition(comp);
        model.setTeam(team);
        CompetitionTeam competitionTeam = updateFields(updateModel.get(), model);
        log.debug("updated CompTeam  with {}", competitionTeam);
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
    public List<CompetitionTeam> getAllForComp(Long compId) {
        return compTeamRepo.getAllForComp(compId);

    }

    @Override
    public List<Team> findUnregisteredTeams(boolean isClub, List<CompetitionTeam> models) {
        List<Team> allTeams;
        if (isClub) {
            allTeams = this.teamRepo.findAllClubTeams();
        } else {
            allTeams = this.teamRepo.findAllNationTeams();
        }
        for (CompetitionTeam ct : models) {
            Team teamToEvaluate = ct.getTeam();
            if (allTeams.contains(teamToEvaluate)) {
                allTeams.remove(ct.getTeam());
            }
        }

        return allTeams;
    }

    @Override
   public Optional<CompetitionTeam> findByTeamIdAndCompId(Long teamId, Long compId){
       return this.compTeamRepo.findByTeamIdAndCompId(teamId, compId);
    }
}
