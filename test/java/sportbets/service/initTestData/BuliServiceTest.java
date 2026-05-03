package sportbets.service.initTestData;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BuliServiceTest {


    private static final Logger log = LoggerFactory.getLogger(BuliServiceTest.class);


    @Autowired
    BuliService buliService;

    @Test
    @Order(1)
    public void saveBuliData() {
        log.debug("saveBuliData");
   //     buliService.execute();

    }
}
