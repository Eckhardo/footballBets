package sportbets.service.tipps;

import org.springframework.stereotype.Service;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.web.dto.competition.CompetitionFamilyDto;

import java.util.List;
import java.util.Optional;

@Service
public interface TippModusService {


    Optional<TippModus> findById(Long id);

    TippModus save(TippModus tippModus);

    Optional<TippModus> update(Long id,TippModus tippModus);

    void deleteByName(String name);

    void deleteById(Long id);

    List<TippModus> getAll();

    Optional<TippModus> findByByName(String name);

    Optional<CompetitionFamily> findByByCompId(Long id);

}
