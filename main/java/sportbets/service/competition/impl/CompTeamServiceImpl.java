package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
    public CompTeamServiceImpl(CompetitionTeamRepository compTeamRepo, CompetitionRepository compRepo, TeamRepository teamRepo) {
        this.compTeamRepo = compTeamRepo;
        this.compRepo = compRepo;
        this.teamRepo = teamRepo;

    }

    /**
     * @param id - the com team id
     * @return - return optional of a competition team
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

        final Optional<CompetitionTeam> entity = getCompetitionTeam(compTeamDto);
        if (entity.isPresent()) {
            throw new EntityExistsException("CompTeam  already exist with given id:" + compTeamDto.getId());
        }
        final Competition comp = getCompetition(compTeamDto);
        final Team team = getTeam(compTeamDto);
        return compTeamRepo.save(new CompetitionTeam(team, comp));


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
            final Optional<CompetitionTeam> entity = getCompetitionTeam(compTeamDto);

            if (entity.isPresent()) {
                log.error("CompetitionTeam already exists");
                throw new EntityExistsException("CompTeam  already exist with given id:" + compTeamDto.getId());
            }
            final Competition comp = getCompetition(compTeamDto);
            final Team team = getTeam(compTeamDto);
            savedTeams.add(compTeamRepo.save(new CompetitionTeam(team, comp)));
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
        CompetitionTeam updateModel = compTeamRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("compTeam  does not exits given id:" + competitionTeamDto.getId()));

        final Competition comp = getCompetition(competitionTeamDto);
        final Team team = getTeam(competitionTeamDto);
        updateModel.setTeam(team);
        updateModel.setCompetition(comp);
        return Optional.of(compTeamRepo.save(updateModel));


    }

    /**
     * @param ids - the com team ids to be deleted
     */
    @Override
    @Transactional
    public void deleteAll(List<Long> ids) {
        for (Long id : ids) {
          this.deleteById(id);
        }
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        // Idempotent check: If it is already gone, do nothing
        if (compTeamRepo.existsById(id)) {
            compTeamRepo.deleteById(id);
        }
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Optional<CompetitionTeam> findByTeamIdAndCompId(Long teamId, Long compId) {
        return compTeamRepo.findByTeamIdAndCompId(teamId, compId);
    }

    private Team getTeam(CompetitionTeamDto compTeamDto) {
        return teamRepo.findById(compTeamDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Competition getCompetition(CompetitionTeamDto compTeamDto) {
        return compRepo.findByName(compTeamDto.getCompName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Optional<CompetitionTeam> getCompetitionTeam(CompetitionTeamDto compTeamDto) {
        return compTeamRepo.findByTeamIdAndCompId(compTeamDto.getTeamId(), compTeamDto.getCompId());
    }

}
