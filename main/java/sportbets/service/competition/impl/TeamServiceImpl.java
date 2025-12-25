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
import sportbets.web.dto.MapperUtil;
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
        Team team = teamRepository.findById(id).orElse(null);
        return Optional.ofNullable(modelMapper.map(team, TeamDto.class));
    }

    /**
     * @param name
     * @return
     */
    @Override
    public Optional<Team> findByName(String name) {
        return teamRepository.findByName(name);

    }

    @Override
    @Transactional
    public Optional<TeamDto> save(TeamDto teamDto) {
        Optional<Team> savedTeam = teamRepository.findByName(teamDto.getName());
        if (savedTeam.isPresent()) {
            throw new EntityExistsException("Team already exist with given name:" + teamDto.getName());
        }

        Team model = modelMapper.map(teamDto, Team.class);
        log.info("model be save:: {}", model);
        Team createdModel = teamRepository.save(model);
        log.info("saved entity:: {}", createdModel);
        TeamDto createdDto = modelMapper.map(createdModel, TeamDto.class);
        log.info("dto to return:: {}", createdDto);
        return Optional.of(createdDto);
    }

    @Override
    @Transactional
    public Optional<TeamDto> updateTeam(Long id, TeamDto teamDto) {
        log.info("updateDto:: {}", teamDto);
        Optional<Team> updateModel = teamRepository.findById(id);

        if (updateModel.isEmpty()) {
            throw new EntityNotFoundException("Team  with given name not found:" + teamDto.getName());
        }
        Optional<Team> updated = updateModel.map(base -> updateFields(base, teamDto))
                .map(teamRepository::save);
        TeamDto famDto = modelMapper.map(updated, TeamDto.class);
        return Optional.of(famDto);

    }

    private Team updateFields(Team base, TeamDto updatedTeam) {
        base.setName(updatedTeam.getName());
        base.setAcronym(updatedTeam.getAcronym());
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

        List<Team> comps = teamRepository.findAll();
        List<TeamDto> teamDtos = new ArrayList<>();
        ModelMapper myMapper = MapperUtil.getModelMapperForFamily();
        comps.forEach(team -> {
            teamDtos.add(myMapper.map(team, TeamDto.class));
        });
        return teamDtos;
    }
}
