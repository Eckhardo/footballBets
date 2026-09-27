package sportbets.service.tipps;

import sportbets.persistence.rowObject.TippRow;
import sportbets.persistence.rowObject.TippsRow;
import sportbets.web.dto.tipps.TippDto;

import java.util.List;
import java.util.Optional;


public interface TippService {


    Optional<TippDto> findById(Long id);
   void createOrUpdateRowList(Long spieltagId, List<TippRow> rows);

    void deleteAll();
    void deleteById(Long id);

    List<TippRow> findEmptyTippRowsForTipper(Long spieltagId);

    List<TippRow> findTippRowsForTipper(Long spieltagId, Long commMembId);

    List<TippsRow> findTippsRowsForCommunity(Long spieltagId, Long commId);


}
