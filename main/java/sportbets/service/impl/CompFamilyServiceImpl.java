package sportbets.service.impl;

import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.entity.Team;
import sportbets.persistence.repository.CompetitionFamilyRepository;
import sportbets.service.CompFamilyService;

import java.util.List;
import java.util.Optional;

@Service
public class CompFamilyServiceImpl implements CompFamilyService {

    private CompetitionFamilyRepository compFamilyRepository;

    public CompFamilyServiceImpl(CompetitionFamilyRepository compFamilyRepository) {
        this.compFamilyRepository = compFamilyRepository;
    }

    @Override
    public List<CompetitionFamily> getAll() {
        return compFamilyRepository.findAll();
    }

    @Override
    public Optional<CompetitionFamily> findById(Long id) {
        return compFamilyRepository.findById(id);
    }

    @Override
    public CompetitionFamily save(CompetitionFamily compFam) {
        Optional<CompetitionFamily> savedCompFamily = compFamilyRepository.findByName(compFam.getName());
        if(savedCompFamily.isPresent()){
            throw new EntityExistsException("CompFamily already exist with given name:" + compFam.getName());
        }
        return compFamilyRepository.save(compFam);
    }

    @Override
    public Optional<CompetitionFamily> updateFamily(Long id, CompetitionFamily compFam) {
        return compFamilyRepository.findById(id)
                .map(base -> updateFields(base, compFam))
                .map(compFamilyRepository::save);
    }

    @Override
    public List<Team> findTeams(Long id) {
        return compFamilyRepository.findTeamsForCompFamily(id);
    }


    private CompetitionFamily updateFields(CompetitionFamily base, CompetitionFamily updatedFam) {
        base.setName(updatedFam.getName());
        base.setDescription(updatedFam.getDescription());
        base.setHasClubs(updatedFam.isHasClubs());
        base.setHasLigaModus(updatedFam.isHasLigaModus());
        return base;
    }

    @Override
    public void deleteByName(String name) {
        compFamilyRepository.deleteByName(name);
    }

    @Override
    public void deleteById(Long id) {
        compFamilyRepository.deleteById(id);
    }
}
