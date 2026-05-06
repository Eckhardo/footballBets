package sportbets.service.tipps;

import sportbets.web.dto.tipps.TippModusDto;

import java.util.List;
import java.util.Optional;


public interface TippModusService {


    Optional<TippModusDto> findById(Long id);

    TippModusDto save(TippModusDto dto);

    Optional<TippModusDto> update(Long id,TippModusDto dto);

    void deleteById(Long id);

    List<TippModusDto> getAllForCommunity(Long id);

   List<TippModusDto> findTotoTypesForCommunity(Long id);

    List<TippModusDto> findResultTypesForCommunity(Long id);


    List<TippModusDto> findPointTypesForCommunity(Long id);
    Optional<TippModusDto> findByNameAndCommunity(String name, Long commId);

}
