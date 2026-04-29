package sportbets.web.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.tipps.TippModusPoint;
import sportbets.persistence.entity.tipps.TippModusResult;
import sportbets.persistence.entity.tipps.TippModusToto;
import sportbets.web.dto.tipps.TippModusPointDto;
import sportbets.web.dto.tipps.TippModusResultDto;
import sportbets.web.dto.tipps.TippModusTotoDto;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TippModelMapperTest {
    private static final Logger log = LoggerFactory.getLogger(TippModelMapperTest.class);

    private static final Community TEST_COMMUNITY = new Community("TestComm", "Description of test community");

    @BeforeAll
    public static void setUp() {
        TEST_COMMUNITY.setId(10L);
    }

    @Test
    public void convertTippModusTotoToDTO() {
        log.info("\n convert tippmodus toto");

        final ModelMapper myMapper = new MapperUtilTipps().modelMapperForTotoTipp();
        Collection<TypeMap<?, ?>> typeMaps = myMapper.getTypeMaps();
        for (TypeMap<?, ?> typeMap : typeMaps) {
            log.info("typeMap: " + typeMap.getMappings());
        }

        TippModusToto entity = new TippModusToto("myNameToto",TippModusType.TIPPMODUS_TOTO, 1, TEST_COMMUNITY);
        entity.setId(20L);
        TippModusTotoDto dto = myMapper.map(entity, TippModusTotoDto.class);
        assertEquals(entity.getCommunity().getName(), dto.getCommName());
        assertEquals(entity.getType().getDisplayName(), dto.getType());
        assertEquals(entity.getId(), dto.getId());
    }

    @Test
    public void convertTippModusResultToDTO() {
        log.info("\n convert tippmodus result");
        final ModelMapper myMapper = new MapperUtilTipps().modelMapperForResultTipp();


        TippModusResult entity = new TippModusResult("myNameResult",TippModusType.TIPPMODUS_RESULT, 1, TEST_COMMUNITY, 3, 1);
        entity.setId(20L);
        TippModusResultDto dto = myMapper.map(entity, TippModusResultDto.class);
        assertEquals(entity.getCommunity().getName(), dto.getCommName());
        assertEquals(entity.getType().getDisplayName(), dto.getType());
        assertEquals(entity.getId(), dto.getId());


    }


    @Test
    public void convertTippModusPointToDTO() {
        log.info("\n convert tippmodus point");
        final ModelMapper myMapper = new MapperUtilTipps().modelMapperForPointTipp();


        TippModusPoint entity = new TippModusPoint("myNamePoint",TippModusType.TIPPMODUS_POINT, 1, TEST_COMMUNITY, 4);
        entity.setId(20L);
        TippModusPointDto dto = myMapper.map(entity, TippModusPointDto.class);
        assertEquals(entity.getCommunity().getName(), dto.getCommName());
        assertEquals(entity.getType().getDisplayName(), dto.getType());
        assertEquals(entity.getId(), dto.getId());
    }
}