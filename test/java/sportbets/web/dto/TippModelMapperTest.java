package sportbets.web.dto;

import org.junit.jupiter.api.Test;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.common.Country;
import sportbets.common.TippModusType;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.TippModusResult;
import sportbets.persistence.entity.tipps.TippModusToto;
import sportbets.web.dto.competition.CompetitionFamilyDto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sportbets.web.dto.ModelMapperEntityDtoTest.TEST_FAM;

public class TippModelMapperTest {
    private static final Logger log = LoggerFactory.getLogger(TippModelMapperTest.class);

    private static Community TEST_COMMUNITY = new Community("TestComm", "Description of test community");


    @Test
    public void convertTippModusTotoToDTO() {
        log.info("\n convert tippmodus toto");
        TEST_COMMUNITY.setId(10L);
        final ModelMapper myMapper =new MapperUtilTipps().modelMapperForTotoTipp();


        TippModusToto entity = new TippModusToto(TippModusType.TIPPMODUS_TOTO, 1,TEST_COMMUNITY);
        entity.setId(20L);
        TippModusTotoDto dto = myMapper.map(entity, TippModusTotoDto.class);
        assertEquals(dto.getType(), entity.getType().getDisplayName());
        assertEquals(dto.getCommName(), entity.getCommunity().getName());

        log.info("dto:: {}", dto.toString());

    }

    @Test
    public void convertTippModusResultToDTO() {
        log.info("\n convert tippmodus result");
        TEST_COMMUNITY.setId(10L);
        final ModelMapper myMapper = new MapperUtilTipps().modelMapperForResultTipp();


        TippModusResult entity = new TippModusResult(TippModusType.TIPPMODUS_RESULT, 1,TEST_COMMUNITY,3,1);
        entity.setId(20L);
        TippModusResultDto dto = myMapper.map(entity, TippModusResultDto.class);
        assertEquals(dto.getType(), entity.getType().getDisplayName());
        assertEquals(dto.getCommName(), entity.getCommunity().getName());
        log.info("dto:: {}", dto.toString());

    }

    @Test
    public void convertTippModusPointToDTO() {
        log.info("\n convert tippmodus point");
        TEST_COMMUNITY.setId(10L);
        final ModelMapper myMapper = new MapperUtilTipps().modelMapperForPointTipp();


        TippModusPoint entity = new TippModusPoint(TippModusType.TIPPMODUS_POINT, 1,TEST_COMMUNITY,4);
        entity.setId(20L);
        TippModusPointDto dto = myMapper.map(entity, TippModusPointDto.class);
        assertEquals(dto.getType(), entity.getType().getDisplayName());
        assertEquals(dto.getCommName(), entity.getCommunity().getName());

        log.info("dto:: {}", dto.toString());

    }

}
