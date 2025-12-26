package sportbets.service.competition.impl;

import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.repository.competition.CompetitionFamilyRepository;
import sportbets.service.competition.CompFamilyService;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CompFamilyServiceImpl implements CompFamilyService {

    private static final Logger log = LoggerFactory.getLogger(CompFamilyServiceImpl.class);
    private final CompetitionFamilyRepository compFamilyRepository;
    private final ModelMapper modelMapper;

    public CompFamilyServiceImpl(CompetitionFamilyRepository compFamilyRepository, ModelMapper modelMapper) {
        this.compFamilyRepository = compFamilyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CompetitionFamily> getAll() {

        return compFamilyRepository.findAll();

    }

    /**
     * @param familyId
     * @return
     */
    @Override
    @Transactional
    public Optional<CompetitionFamily> findByIdTest(Long familyId) {
        Optional<CompetitionFamily> model = compFamilyRepository.findById(familyId);
        if (model.isPresent()) {
            Set<Competition> comps = model.get().getCompetitions();
            assert !comps.isEmpty();
            for (Competition comp : comps) {
                log.info("Competition of current family found with {}", comp);
            }
        }
        return model;

    }

    @Override
    public Optional<CompetitionFamily> findById(Long id) {
       return compFamilyRepository.findById(id);

    }

    @Override
    @Transactional
    public Optional<CompetitionFamily> save(CompetitionFamily compFam) {
        Optional<CompetitionFamily> savedCompFamily = compFamilyRepository.findByName(compFam.getName());
        if (savedCompFamily.isPresent()) {
            throw new EntityExistsException("CompFamily already exist with given name:" + compFam.getName());
        }
        return Optional.of(compFamilyRepository.save(compFam));

    }

    @Override
    @Transactional
    public Optional<CompetitionFamily> updateFamily(Long id, CompetitionFamily compFam) {
        log.info("updateFamily:: {}", compFam);
        Optional<CompetitionFamily> updateModel = compFamilyRepository.findById(id);

        if (updateModel.isPresent()) {
            return updateModel.map(base -> updateFields(base, compFam))
                    .map(compFamilyRepository::save);
        } else {
            return Optional.empty();
        }

    }


    private CompetitionFamily updateFields(CompetitionFamily base, CompetitionFamily updatedFam) {
        base.setName(updatedFam.getName());
        base.setDescription(updatedFam.getDescription());
        base.setHasClubs(updatedFam.isHasClubs());
        base.setHasLigaModus(updatedFam.isHasLigaModus());
        return base;
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        compFamilyRepository.deleteByName(name);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        compFamilyRepository.deleteById(id);
    }

}
