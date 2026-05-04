package sportbets.service.tipps;

import org.springframework.stereotype.Service;
import sportbets.web.dto.tipps.TippModusDto;

import java.util.Optional;


public interface TippService {



    Optional<TippModusDto> findById(Long id);

    TippModusDto save(TippModusDto dto);

    Optional<TippModusDto> update(Long id,TippModusDto dto);

    void deleteById(Long id);

}
