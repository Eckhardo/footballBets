package sportbets.web.dto;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.entity.CompetitionRound;
import sportbets.persistence.entity.Spieltag;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

class CompetitionFamilyDtoTest {

    private static final Logger log = LoggerFactory.getLogger(CompetitionFamilyDtoTest.class);
    ModelMapper modelMapper = new ModelMapper();
    @Test
    void checkModelMapper() {
        CompetitionFamily testFamily = new CompetitionFamily("2. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);

        Competition testComp = new Competition("Saison 2025/26", "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        CompetitionRound testRound = new CompetitionRound(1, "Hinrunde", testComp, false);



        CompetitionRoundDto compRoundDto = modelMapper.map(testRound, CompetitionRoundDto.class);
        log.info("Round:: " + compRoundDto.toString());
        CompetitionDto compDto = modelMapper.map(testComp, CompetitionDto.class);
        log.info("Comp:: " + compDto.toString());

        CompetitionFamilyDto famDto = modelMapper.map(testFamily, CompetitionFamilyDto.class);
        log.info("Family:: " + famDto.toString());


    }


    @Test
    public void checkFamily() {
        log.info("\n validate family");
        modelMapper.createTypeMap(CompetitionFamilyDto.class, CompetitionFamily.class);
        modelMapper.validate();
        CompetitionFamily testFamily = new CompetitionFamily("2. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);
        CompetitionFamilyDto famDto = modelMapper.map(testFamily, CompetitionFamilyDto.class);
        log.info("Family:: {}", famDto.toString());

    }

    @Test
    public void checkComp() {
        log.info("\n validate competition");

        CompetitionFamily testFamily = new CompetitionFamily("2. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);
        testFamily.setId(10L);
        Competition testComp = new Competition("Saison 2025/26", "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testComp.setId(5L);
        CompetitionDto compDto = modelMapper.map(testComp, CompetitionDto.class);

        log.info("Comp:: " + compDto.toString());
    }

    @Test
    public void checkRound() {
        log.info("\n validate comp round");

        CompetitionFamily testFamily = new CompetitionFamily("2. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);
        testFamily.setId(10L);
        Competition testComp = new Competition("Saison 2025/26", "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testComp.setId(5L);
        CompetitionRound testRound = new CompetitionRound(1, "Hinrunde", testComp, false);
        CompetitionRoundDto compRoundDto = modelMapper.map(testRound, CompetitionRoundDto.class);
        log.info("Round:: " + compRoundDto.toString());

    }

    @Test
    public void checkSpieltag() {
        log.info("\n validate comp round");


        CompetitionFamily testFamily = new CompetitionFamily("2. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);
        testFamily.setId(10L);
        Competition testComp = new Competition("Saison 2025/26", "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        testComp.setId(5L);
        CompetitionRound testRound = new CompetitionRound(1, "Hinrunde", testComp, false);
        testRound.setId(7L);
        Spieltag testSpieltag = new Spieltag(1, LocalDateTime.now(), testRound);
        Spieltag testSpieltag2 = new Spieltag(2, LocalDateTime.now(), testRound);
        Spieltag testSpieltag3 = new Spieltag(4, LocalDateTime.now(), testRound);
        List<Spieltag> spieltage = List.of(testSpieltag, testSpieltag2, testSpieltag3);

        spieltage.forEach(spieltag -> {
            SpieltagDto spieltagDto = modelMapper.map(spieltag, SpieltagDto.class);
            log.info("Spieltag:: " + spieltagDto.toString());
        });


    }
}