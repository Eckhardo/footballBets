package sportbets.service.tipps;


import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.service.community.CommunityService;
import sportbets.testdata.TestConstants;
import sportbets.web.dto.community.CommunityDto;
import sportbets.web.dto.tipps.TippModusDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TippModusServiceTest {

    private static final Logger log = LoggerFactory.getLogger(TippModusServiceTest.class);

    private final CommunityDto communityDto = TestConstants.createValidCommunityDto();
    private final CommunityDto communityDto2 = TestConstants.createValidCommunityDto2();
    private final TippModusTotoDto tippModusTotoDto = TestConstants.createValidTippModusTotoDto();
    private final TippModusResultDto tippModusResultDto = TestConstants.createValidTippModusResultDto();
    private final TippModusPointDto tippModusPointDto = TestConstants.createValidTippModusPointDto();

    Community savedCommunity = null;


    @Autowired
    CommunityService communityService;
    @Autowired
    TippModusService tippModusService;

    @BeforeEach
    public void setup() {
        log.debug("setup: {}", communityDto);
        savedCommunity = communityService.save(communityDto);
        tippModusPointDto.setCommId(savedCommunity.getId());
        tippModusPointDto.setCommName(savedCommunity.getName());
        tippModusResultDto.setCommId(savedCommunity.getId());
        tippModusResultDto.setCommName(savedCommunity.getName());
        tippModusTotoDto.setCommId(savedCommunity.getId());
        tippModusTotoDto.setCommName(savedCommunity.getName());

    }

    @AfterEach
    public void tearDown() {
        communityService.deleteById(savedCommunity.getId());

    }

    @Test
    @Order(1)
    public void ifTippModusDtosAreSaved_then_ReturningDtosAreEqual() {
        log.debug("ifTippModusDtosAreSaved_then_RetruningDtosAreEqual");

        TippModusTotoDto dto = (TippModusTotoDto) tippModusService.save(tippModusTotoDto);
        assertEquals(tippModusTotoDto.getCommName(), dto.getCommName());
        assertEquals(tippModusTotoDto.getType(), dto.getType());
        assertEquals(tippModusTotoDto.getName(), dto.getName());
        assertNotNull(dto.getId());
        log.info("totoType:{}", dto);

        log.debug("saveTippModus Result");
        TippModusResultDto dtoResult = (TippModusResultDto) tippModusService.save(tippModusResultDto);
        assertEquals(tippModusResultDto.getCommName(), dtoResult.getCommName());
        assertEquals(tippModusResultDto.getType(), dtoResult.getType());
        assertEquals(tippModusResultDto.getName(), dtoResult.getName());
        assertNotNull(dtoResult.getId());
        assertEquals(tippModusResultDto.getDeadline(), dtoResult.getDeadline());

        TippModusResultDto resultDto = tippModusResultDto;
        assertEquals(resultDto.getBonusPoints(), dtoResult.getBonusPoints());
        assertEquals(resultDto.getTendencyPoints(), dtoResult.getTendencyPoints());
        log.info("resultType:{}", dto);


        TippModusPointDto pointDto = (TippModusPointDto) tippModusService.save(tippModusPointDto);
        assertEquals(tippModusPointDto.getCommName(), pointDto.getCommName());
        assertEquals(tippModusPointDto.getType(), pointDto.getType());
        assertEquals(tippModusPointDto.getName(), pointDto.getName());
        assertNotNull(pointDto.getId());
        assertNotNull(pointDto.getTotalPoints());
        log.info("pointType:{}", dto);
    }


    @Test
    @Order(2)
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

        // prepare for update
        TippModusResultDto resultDto = tippModusResultDto;
        assertEquals(resultDto.getBonusPoints(), dtoResult.getBonusPoints());
        assertEquals(resultDto.getTendencyPoints(), dtoResult.getTendencyPoints());

        dtoResult.setTendencyPoints(5);
        TippModusResultDto updatedResult = (TippModusResultDto) tippModusService.update(dtoResult.getId(), dtoResult).orElseThrow(() -> new RuntimeException("update failed"));
        assertEquals(dtoResult.getTendencyPoints(), updatedResult.getTendencyPoints());

        log.info("resultType:{}", dtoResult);
    }

    @Test
    @Order(3)
    public void ifTippModusDtosAreSaved_then_RetrievingThemSucceeds() {
        log.debug("ifTippModusDtosAreSaved_then_RetrievingThemSucceeds");

        tippModusService.save(tippModusPointDto);
        tippModusService.save(tippModusTotoDto);
        tippModusService.save(tippModusResultDto);
        List<TippModusDto> dtos = tippModusService.getAllForCommunity(savedCommunity.getId());
        assertEquals(3, dtos.size());
    }

    @Test
    @Order(4)
    public void ifTippModusDtosAreSaved_then_RetrievingTypesSelectivelySucceeds() {
        log.debug("ifTippModusDtosAreSaved_then_RetrievingTypesSelectivelySucceeds");

        //given
        tippModusService.save(tippModusTotoDto);
        tippModusService.save(tippModusResultDto);
        tippModusService.save(tippModusPointDto);

        List<TippModusDto> totos = tippModusService.findTotoTypesForCommunity(savedCommunity.getId());
        assertEquals(1, totos.size());

        List<TippModusDto> resultDtos = tippModusService.findResultTypesForCommunity(savedCommunity.getId());
        assertEquals(1, resultDtos.size());

        List<TippModusDto> pointDtos = tippModusService.findPointTypesForCommunity(savedCommunity.getId());
        assertEquals(1, pointDtos.size());
    }
}
