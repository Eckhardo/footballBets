package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Team;
import sportbets.persistence.repository.competition.TeamRepository;
import sportbets.service.competition.TeamService;

import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {


    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);

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
    public Team save(Team team) {
        Optional<Team> savedTeam = teamRepository.findByName(team.getName());
        if (savedTeam.isPresent()) {
            throw new EntityExistsException("Team already exist with given name:" + team.getName());
        }

        return teamRepository.save(team);
    }

    @Override
    @Transactional
    public Optional<Team> updateTeam(Long id, Team team) {
        log.info("updateDto:: {}", team);
        Optional<Team> updateModel = teamRepository.findById(id);


        if (updateModel.isEmpty()) {
            throw new EntityExistsException("Team  already exist with given name:" + team.getName());
        }

        Team updated = updateFields(updateModel.get(), team);
        log.info("updated Team  with {}", updated);
        return Optional.of(teamRepository.save(updated));

    }

    private Team updateFields(Team base, Team updatedTeam) {
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
    public List<Team> getAll() {

        return teamRepository.findAll();

    }
}
