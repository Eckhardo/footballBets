package sportbets.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.repository.CompetitionRepository;
import sportbets.service.CompService;

import java.util.List;
import java.util.Optional;
@Service
public class CompServiceImpl implements CompService {

    private CompetitionRepository compRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CompServiceImpl(CompetitionRepository compRepository) {
        this.compRepository = compRepository;
    }

    @Override
    public Optional<Competition> findById(Long id) {
        return compRepository.findById(id);
    }

    @Override
    public Competition save(Competition comp) {
        comp.setId(null);
        return compRepository.save(comp);
    }

    @Override
    public Optional<Competition> updateComp(Long id, Competition comp) {
        return compRepository.findById(id)
                .map(base -> updateFields(base, comp))
                .map(compRepository::save);
    }

    @Override
    public void deleteById(Long id) {
        compRepository.deleteById(id);
    }

    @Override
    public List<Competition> getAll() {
        return compRepository.findAll();
    }

    private Competition updateFields(Competition base, Competition updatedComp) {
        base.setName(updatedComp.getName());
        base.setDescription(updatedComp.getDescription());
        base.setWinMultiplicator(updatedComp.getWinMultiplicator());
        base.setRemisMultiplicator(updatedComp.getRemisMultiplicator());
        return base;
    }
}
