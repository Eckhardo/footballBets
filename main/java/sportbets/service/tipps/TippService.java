package sportbets.service.tipps;

import sportbets.persistence.rowObject.TippRow;
import sportbets.web.dto.tipps.TippDto;

import java.util.List;
import java.util.Optional;


public interface TippService {


    Optional<TippDto> findById(Long id);

    TippDto saveOne(TippDto dto);

    List<TippDto> saveList(List<TippDto> dtoList);


    Optional<TippDto> updateOne(Long id, TippDto dto);

    List<TippDto> updateList(List<TippDto> dtoList);


    void deleteById(Long id);

    List<TippRow> findTippRows(Long id);
}
