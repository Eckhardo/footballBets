package sportbets.testdata;

import sportbets.persistence.entity.competition.CompetitionFamily;
import sportbets.web.dto.competition.CompetitionDto;
import sportbets.web.dto.competition.CompetitionFamilyDto;

public class TestConstants {
    

    public static CompetitionFamily BUNDESLIGA = new CompetitionFamily("1. Bundesliga", "1. Deutsche Fussball Bundesliga", true, true);
    public static CompetitionFamily FIFA_WM = new CompetitionFamily("FIFA WM", "Fussball Weltmeisterschaft", false, false);
    public static CompetitionFamilyDto TEST_FAMILY = new CompetitionFamilyDto(null, "TEST FAMILY", "description of testliga", true, true);
    public static CompetitionDto TEST_COMP = new CompetitionDto(null, "TestLiga: Saison 2025", "Description of Competition", 3, 1, null, TEST_FAMILY.getName());
   }
