package sportbets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sportbets.persistence.repository.*;

@SpringBootApplication
public class FootballBetsApplication {

    public static void main(String[] args) {

        SpringApplication.run(FootballBetsApplication.class, args);
    }

    @Autowired
    private CompetitionFamilyRepository familyRepo;
    @Autowired
    private CompetitionTeamRepository compTeamRepo;
    @Autowired
    private CompetitionRepository compRepo;
    @Autowired
    private CompetitionRoundRepository compRoundRepo;

    @Autowired
    private SpieltagRepository spieltagRepo;
    @Autowired
    private SpielRepository spielRepo;

    @Autowired
    private TeamRepository teamRepo;

    @Bean
    public CommandLineRunner run(String[] args) {
        return runner -> {

//            CompetitionFamily fam = familyRepo.save(CompFamilyConstants.BUNDESLIGA);
//            Competition comp = compRepo.save(CompetitionConstants.getBundesliga2025(fam));
//
//            CompetitionRound hinRunde = compRoundRepo.save(CompRoundConstants.getHinrunde(comp));
//            CompetitionRound rueckRunde = compRoundRepo.save(CompRoundConstants.getRueckrunde(comp));
//            Team bay = TeamConstants.BAY;
//            Team hsv = TeamConstants.HSV;
//            Team pauli = TeamConstants.PAULI;
//            Team werder = TeamConstants.WERDER;
//            Team bvb = TeamConstants.BVB;
//            Team koe = TeamConstants.KOELN;
//            Team wob = TeamConstants.WOLFSBURG;
//            Team lev = TeamConstants.LEVERKUSEN;
//            Team vfb = TeamConstants.STUTTGART;
//            Team freiburg = TeamConstants.FREIBURG;
//            Team frankfurt = TeamConstants.FRANKFURT;
//            Team hoffenheim = TeamConstants.HOFFENHEIM;
//            Team augsburg = TeamConstants.AUGSBURG;
//            Team union = TeamConstants.UNION;
//            Team mainz = TeamConstants.MAINZ;
//            Team heidenheim = TeamConstants.HEIDENHEIM;
//            Team leipzig = TeamConstants.LEIPZIG;
//            Team gladbach = TeamConstants.GLADBACH;
//
//            teamRepo.saveAll(List.of(bay, hsv, pauli, werder, bvb, koe, wob, lev, vfb, frankfurt, freiburg, hoffenheim, augsburg, union, mainz, heidenheim, leipzig, gladbach));
//            //  testGroup = new CompetitionGroup("Gruppe A", 1, testRound);
//
//            compTeamRepo.saveAll(List.of(new CompetitionTeam(bay, comp),
//                    new CompetitionTeam(hsv, comp),
//                    new CompetitionTeam(pauli, comp),
//                    new CompetitionTeam(werder, comp),
//                    new CompetitionTeam(bvb, comp),
//                    new CompetitionTeam(koe, comp),
//                    new CompetitionTeam(wob, comp),
//                    new CompetitionTeam(lev, comp),
//                    new CompetitionTeam(vfb, comp),
//                    new CompetitionTeam(freiburg, comp),
//                    new CompetitionTeam(frankfurt, comp),
//                    new CompetitionTeam(hoffenheim, comp),
//                    new CompetitionTeam(augsburg, comp),
//                    new CompetitionTeam(union, comp),
//                    new CompetitionTeam(mainz, comp),
//                    new CompetitionTeam(heidenheim, comp),
//                    new CompetitionTeam(leipzig, comp),
//                    new CompetitionTeam(gladbach, comp)
//            ));
//
//            List<Spieltag> spieltagHin = spieltagRepo.saveAll(SpieltagConstants.getSpieltageHinrunde(hinRunde));
//
//            List<Spiel> spieleHin = spielRepo.saveAll(SpielConstants.getSpieleHinrunde(spieltagHin, pauli, hsv));
//
//
//            List<Spieltag> spieltagRueck = spieltagRepo.saveAll(SpieltagConstants.getSpieltageRueckrunde(rueckRunde));
//
//            List<Spiel> spieleRueck = spielRepo.saveAll(SpielConstants.getSpieleRückrunde(spieltagRueck, werder, bay));
//

            System.out.println("Save all cascade");
        };
    }
}

