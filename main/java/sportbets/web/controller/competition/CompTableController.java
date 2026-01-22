package sportbets.web.controller.competition;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sportbets.persistence.rowObject.TeamPositionSummaryRow;
import sportbets.service.competition.CompTableService;
import sportbets.web.dto.competition.search.TableSearchCriteria;

import java.util.List;

@RestController
public class CompTableController {

    private static final Logger log = LoggerFactory.getLogger(CompTableController.class);
    private final CompTableService compTableService;

    public CompTableController(CompTableService compTableService) {
        this.compTableService = compTableService;
    }



    @GetMapping("/compTable/search")
    public List<TeamPositionSummaryRow> searchHeimGast(TableSearchCriteria criteria) {
        log.info("CompTableController search {}", criteria);

        if (criteria.getHeimOrGast()!=null) {
            return compTableService.findTableHeimOrGastForLigaModus(criteria);
        } else {
            return compTableService.findTableForLigaModus(criteria);
        }

    }
}
