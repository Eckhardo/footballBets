package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.TeamRepository;
import sportbets.service.competition.TeamService;
import sportbets.web.dto.competition.TeamDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {


    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;

    public TeamServiceImpl(TeamRepository teamRepository, ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<TeamDto> findById(Long id) {

        Team model = teamRepository.findById(id).orElseThrow(() -> new RuntimeException("Team not found"));
        return Optional.of(this.modelMapper.map(model, TeamDto.class));

    }

    /**
     * @param name
     * @return
     */
    @Override
    @Transactional
    public Optional<TeamDto> findByName(String name) {
        Team model = teamRepository.findByName(name).orElseThrow(() -> new RuntimeException("Team not found"));
        return Optional.of(this.modelMapper.map(model, TeamDto.class));
    }

    @Override
    @Transactional
    public TeamDto save(TeamDto teamDto) {

        log.info("Saving team {}", teamDto);
        Optional<Team> entity = teamRepository.findByName(teamDto.getName());
        if (entity.isPresent()) {
            throw new EntityExistsException("Team already exist with given name:" + teamDto.getName());
        }
        log.info("Now: Save team {}", teamDto);
        Team model = this.modelMapper.map(teamDto, Team.class);
        Team savedEntity = teamRepository.save(model);
        log.info(" Saved team {}", savedEntity);
        return this.modelMapper.map(savedEntity, TeamDto.class);
    }

    @Override
    @Transactional
    public Optional<TeamDto> updateTeam(Long id, TeamDto teamDto) {
        log.debug("updateDto:: {}", teamDto);
        Team entity = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found"));


        Team updatedEntity = updateFields(entity, teamDto);
        Team savedEntity = teamRepository.save(updatedEntity);
        log.debug("updated Team  with {}", savedEntity);
        return Optional.of(this.modelMapper.map(savedEntity, TeamDto.class));

    }

    private Team updateFields(Team base, TeamDto updatedTeam) {
        base.setName(updatedTeam.getName());
        base.setAcronym(updatedTeam.getAcronym());
        base.setHasClub(updatedTeam.isHasClub());
        return base;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        teamRepository.deleteByName(name);
    }

    @Override
    public List<TeamDto> getAll() {

        log.info("getAll");

        List<Team> teams = teamRepository.findAll();
        List<TeamDto> teamDtos = new ArrayList<>();
        for (Team team : teams) {
            TeamDto teamDto = this.modelMapper.map(team, TeamDto.class);
            teamDtos.add(teamDto);
        }
        return teamDtos;
    }

    @Override
    public List<TeamDto> getAllClubTeams() {
        List<Team> teams = teamRepository.findAllClubTeams();
        List<TeamDto> teamDtos = new ArrayList<>();
        for (Team team : teams) {
            TeamDto teamDto = this.modelMapper.map(team, TeamDto.class);
            teamDtos.add(teamDto);
        }
        return teamDtos;
    }

    @Override
    public List<TeamDto> getAllNationTeams() {
        List<Team> teams = teamRepository.findAllNationTeams();
        List<TeamDto> teamDtos = new ArrayList<>();
        for (Team team : teams) {
            TeamDto teamDto = this.modelMapper.map(team, TeamDto.class);
            teamDtos.add(teamDto);
        }
        return teamDtos;
    }
}
