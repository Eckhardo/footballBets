package sportbets.service.tipps;

import org.springframework.stereotype.Service;

import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.util.List;
import java.util.Optional;


public interface TippModusService {


    Optional<TippModusDto> findById(Long id);

    TippModusDto save(TippModusDto dto);

    Optional<TippModusDto> update(Long id,TippModusDto dto);

    void deleteById(Long id);

    List<TippModusDto> getAllForCommunity(Long id);

   List<TippModusTotoDto> findTotoTypesForCommunity(Long id);

    List<TippModusResultDto> findResultTypesForCommunity(Long id);


    List<TippModusPointDto> findPointTypesForCommunity(Long id);


}
