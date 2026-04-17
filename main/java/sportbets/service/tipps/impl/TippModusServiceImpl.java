package sportbets.service.tipps.impl;

import org.springframework.stereotype.Service;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.repository.tipps.TippModusRepository;
import sportbets.service.tipps.TippModusService;

import java.util.List;
import java.util.Optional;

@Service
public class TippModusServiceImpl implements TippModusService {
    private final TippModusRepository repo;

    public TippModusServiceImpl(TippModusRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<TippModus> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public TippModus save(TippModus tippModus) {
        return null;
    }

    @Override
    public Optional<TippModus> update(Long id, TippModus tippModus) {
        return Optional.empty();
    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<TippModus> getAll() {
        return List.of();
    }

    @Override
    public Optional<TippModus> findByByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<CompetitionFamily> findByByCompId(Long id) {
        return Optional.empty();
    }
}
