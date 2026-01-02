package sportbets.service.community;


import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.community.Tipper;
import sportbets.web.dto.community.TipperDto;

import java.util.List;
import java.util.Optional;

public interface TipperService {
    Optional<Tipper> findById(Long id);


    Tipper save(TipperDto dto);

    List<Tipper> saveAll(List<TipperDto> dtos);

    Optional<Tipper> update(Long id, TipperDto dto);

    void deleteAll(List<Long> ids);


    void deleteAll();


    void deleteById(Long id);

    void deleteByUserName(String userName);

    List<Tipper> getAllFormComp(Long compId);

    List<Tipper> getAll();
}
