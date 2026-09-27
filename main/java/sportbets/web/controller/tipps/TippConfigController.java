package sportbets.web.controller.tipps;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sportbets.service.tipps.TippConfigService;
import sportbets.web.dto.tipps.TippConfigDto;

@RestController
@RequestMapping("/config")
public class TippConfigController {

    private static final Logger log = LoggerFactory.getLogger(TippConfigController.class);

    private final TippConfigService tippConfigService;


    public TippConfigController(TippConfigService tippConfigService) {
        this.tippConfigService = tippConfigService;
    }

    @GetMapping("/{id}")
    public TippConfigDto findOne(@PathVariable Long id) {
        log.debug(":findOne::{}", id);
        TippConfigDto dto= tippConfigService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.debug(":return dto::{}", dto);
        return dto;
    }

    @GetMapping("/{matchdayId}/compMemb/{compMembId}")
    public TippConfigDto findByMatchdayAndCompMemb(@PathVariable Long matchdayId,@PathVariable Long compMembId) {
        log.debug(":findByMatchdayAndCompMemb::{} {}", matchdayId,compMembId);
        TippConfigDto dto=tippConfigService.findByMatchdayAndCompMemb(matchdayId,compMembId);
        log.debug(":return dto::{}", dto);
        return dto;

    }


}
