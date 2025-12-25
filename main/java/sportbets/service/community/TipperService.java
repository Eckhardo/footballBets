package sportbets.service.community;



import sportbets.web.dto.community.TipperDto;

import java.util.List;
import java.util.Optional;

public interface TipperService {
    Optional<TipperDto> findById(Long id);


    Optional<TipperDto> save(TipperDto dto);

    List<TipperDto> saveAll(List<TipperDto> dtos);

    Optional<TipperDto> update(Long id, TipperDto dto);

    void deleteAll(List<Long> ids);

    void deleteById(Long id);
    void deleteByUserName(String userName);

    List<TipperDto> getAllFormComp(Long compId);

}
