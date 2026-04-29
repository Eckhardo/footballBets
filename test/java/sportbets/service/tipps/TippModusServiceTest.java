package sportbets.service.tipps;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.service.community.CommunityService;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static sportbets.testdata.TestConstants.COMM_TEST;
import static sportbets.testdata.TestConstants.COMM_TEST_2;

@SpringBootTest
@ActiveProfiles("test")
public class TippModusServiceTest {

    private static final Logger log = LoggerFactory.getLogger(TippModusServiceTest.class);

    private final CommunityDto communityDto = new CommunityDto(null, COMM_TEST, "Description of Community");
    private final CommunityDto communityDto2 = new CommunityDto(null, COMM_TEST_2, "Description of Community2");

    Community savedCommunity = null;

    TippModusDto tippModusTotoDto;
    TippModusDto tippModusResultDto;
    TippModusDto tippModusPointDto;

    @Autowired
    CommunityService communityService;
    @Autowired
    TippModusService tippModusService;

    @BeforeEach
    public void setup() {
        log.debug("setup: {}", communityDto);
        savedCommunity = communityService.save(communityDto);
        tippModusTotoDto = new TippModusTotoDto(null, "TotoTest",TippModusType.TIPPMODUS_TOTO.getDisplayName(), 1, savedCommunity.getId(), savedCommunity.getName());
        tippModusResultDto = new TippModusResultDto(null,"ErgebnisTest", TippModusType.TIPPMODUS_RESULT.getDisplayName(), 1, savedCommunity.getId(), savedCommunity.getName(), 3, 1);
        tippModusPointDto = new TippModusPointDto(null,"PunkteTest", TippModusType.TIPPMODUS_POINT.getDisplayName() , 1, savedCommunity.getId(), savedCommunity.getName(), 4);

    }

    @AfterEach
    public void tearDown() {
        communityService.deleteById(savedCommunity.getId());

    }

    @Test
    public void ifTippModusDtosAreSaved_then_ReturningDtosAreEqual() {
        log.debug("ifTippModusDtosAreSaved_then_RetruningDtosAreEqual");

        assertNotNull(savedCommunity);

        TippModusTotoDto dto = (TippModusTotoDto) tippModusService.save(tippModusTotoDto);
        assertEquals(tippModusTotoDto.getCommName(), dto.getCommName());
        assertEquals(tippModusTotoDto.getType(), dto.getType());
        assertNotNull(dto.getId());
        log.info("totoType:{}", dto);

        log.debug("saveTippModus Result");
        TippModusResultDto dtoResult = (TippModusResultDto) tippModusService.save(tippModusResultDto);
        assertEquals(tippModusResultDto.getCommName(), dtoResult.getCommName());
        assertEquals(tippModusResultDto.getType(), dtoResult.getType());
        assertNotNull(dtoResult.getId());
        assertEquals(tippModusResultDto.getDeadline(), dtoResult.getDeadline());

        TippModusResultDto resultDto = (TippModusResultDto) tippModusResultDto;
        assertEquals(resultDto.getBonusPoints(), dtoResult.getBonusPoints());
        assertEquals(resultDto.getTendencyPoints(), dtoResult.getTendencyPoints());
        log.info("resultType:{}", dto);


        TippModusPointDto pointDto = (TippModusPointDto) tippModusService.save(tippModusPointDto);
        assertEquals(tippModusPointDto.getCommName(), pointDto.getCommName());
        assertEquals(tippModusPointDto.getType(), pointDto.getType());
        assertNotNull(pointDto.getId());
        assertNotNull(pointDto.getTotalPoints());
        log.info("pointType:{}", dto);
    }


    @Test
    public void ifTippModusDtosAreUpdated_then_ReturningDtosAreEqual() {
        log.debug("ifTippModusDtosAreUpdated_then_ReturningDtosAreModified");

        assertNotNull(savedCommunity);

        TippModusTotoDto savedTotoDto = (TippModusTotoDto) tippModusService.save(tippModusTotoDto);
        assertEquals(tippModusTotoDto.getCommName(), savedTotoDto.getCommName());
        assertEquals(tippModusTotoDto.getType(), savedTotoDto.getType());
        assertNotNull(savedTotoDto.getId());

        savedTotoDto.setDeadline(5);
        TippModusTotoDto updated = (TippModusTotoDto) tippModusService.update(savedTotoDto.getId(), savedTotoDto).orElseThrow(() -> new RuntimeException("update failed"));
        assertEquals(tippModusTotoDto.getCommName(), updated.getCommName());
        assertEquals(tippModusTotoDto.getType(), updated.getType());
        assertEquals(5, updated.getDeadline());

        log.info("totoType:{}", updated);


        TippModusResultDto dtoResult = (TippModusResultDto) tippModusService.save(tippModusResultDto);
        assertEquals(tippModusResultDto.getCommName(), dtoResult.getCommName());
        assertEquals(tippModusResultDto.getType(), dtoResult.getType());
        assertNotNull(dtoResult.getId());
        assertEquals(tippModusResultDto.getDeadline(), dtoResult.getDeadline());

        TippModusResultDto resultDto = (TippModusResultDto) tippModusResultDto;
        assertEquals(resultDto.getBonusPoints(), dtoResult.getBonusPoints());
        assertEquals(resultDto.getTendencyPoints(), dtoResult.getTendencyPoints());

        dtoResult.setTendencyPoints(5);
        TippModusResultDto updatedResult =(TippModusResultDto) tippModusService.update(dtoResult.getId(), dtoResult).orElseThrow(() -> new RuntimeException("update failed"));
        assertEquals(dtoResult.getTendencyPoints(), updatedResult.getTendencyPoints());

        log.info("resultType:{}", dtoResult);
    }

    @Test
    public void ifTippModusDtosAreSaved_then_RetrievingThemSucceeds() {
        log.debug("ifTippModusDtosAreSaved_then_RetrievingThemSucceeds");


        tippModusService.save(tippModusTotoDto);
        tippModusService.save(tippModusResultDto);
        tippModusService.save(tippModusPointDto);

        List<TippModusDto> dtos = tippModusService.getAllForCommunity(savedCommunity.getId());
        assertEquals(3, dtos.size());


    }

    @Test
    public void ifTippModusDtosAreSaved_then_RetrievingTypesSelectivelySucceeds() {
        log.debug("ifTippModusDtosAreSaved_then_RetrievingTypesSelectivelySucceeds");

        //given
        tippModusService.save(tippModusTotoDto);
        tippModusService.save(tippModusResultDto);
        tippModusService.save(tippModusPointDto);

        List<TippModusTotoDto> totos = tippModusService.findTotoTypesForCommunity(savedCommunity.getId());
        assertEquals(1, totos.size());

        List<TippModusResultDto> resultDtos = tippModusService.findResultTypesForCommunity(savedCommunity.getId());
        assertEquals(1, resultDtos.size());

        List<TippModusPointDto> pointDtos = tippModusService.findPointTypesForCommunity(savedCommunity.getId());
        assertEquals(1, pointDtos.size());


    }
}
