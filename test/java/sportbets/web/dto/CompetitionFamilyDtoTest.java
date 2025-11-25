package sportbets.web.dto;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.Competition;
import sportbets.persistence.entity.CompetitionFamily;
import sportbets.persistence.entity.CompetitionRound;

class CompetitionFamilyDtoTest {

    private static final Logger log = LoggerFactory.getLogger(CompetitionFamilyDtoTest.class);

    @Test
    void checkModelMapper() {
        CompetitionFamily testFamily = new CompetitionFamily("2. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);

        Competition testComp = new Competition("Saison 2005/26", "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        CompetitionRound testRound = new CompetitionRound(1, "Hinrunde", testComp, false);

        ModelMapper modelMapper = new ModelMapper();

        CompetitionRoundDto compRoundDto = modelMapper.map(testRound, CompetitionRoundDto.class);
        log.info("Round:: " + compRoundDto.toString());
        CompetitionDto compDto = modelMapper.map(testComp, CompetitionDto.class);
        log.info("Comp:: " +compDto.toString());

        CompetitionFamilyDto famDto = modelMapper.map(testFamily, CompetitionFamilyDto.class);
        log.info("Family:: " +famDto.toString());


    }


    @Test
    public void checkFamily() {
        log.info("\n validate family");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(CompetitionFamilyDto.class, CompetitionFamily.class);
        modelMapper.validate();
        CompetitionFamily testFamily = new CompetitionFamily("2. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);
        CompetitionFamilyDto famDto = modelMapper.map(testFamily, CompetitionFamilyDto.class);
        log.info("Family:: {}", famDto.toString());

    }
    @Test
    public void checkComp() {
        log.info("\n validate competition");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(CompetitionDto.class, Competition.class);
        modelMapper.validate();

        CompetitionFamily testFamily = new CompetitionFamily("2. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);
        Competition testComp = new Competition("Saison 2005/26", "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        CompetitionDto compDto = modelMapper.map(testComp, CompetitionDto.class);
        log.info("Comp:: " +compDto.toString());
    }
    @Test
    public void checkRound() {
        log.info("\n validate comp round");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(CompetitionFamilyDto.class, CompetitionFamily.class);
        modelMapper.validate();
        CompetitionFamily testFamily = new CompetitionFamily("2. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);
        Competition testComp = new Competition("Saison 2005/26", "1. Deutsche Fussball Bundesliga Saison 2025/26", 3, 1, testFamily);
        CompetitionRound testRound = new CompetitionRound(1, "Hinrunde", testComp, false);
        CompetitionRoundDto compRoundDto = modelMapper.map(testRound, CompetitionRoundDto.class);
        log.info("Round:: " + compRoundDto.toString());

    }
}