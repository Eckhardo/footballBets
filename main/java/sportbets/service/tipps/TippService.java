package sportbets.service.tipps;

import sportbets.web.dto.tipps.TippDto;

import java.util.Optional;


public interface TippService {



    Optional<TippDto> findById(Long id);

    TippDto save(TippDto dto);

    Optional<TippDto> update(Long id,TippDto dto);

    void deleteById(Long id);

}
