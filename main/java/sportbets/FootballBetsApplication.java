package sportbets;

import org.modelmapper.ModelMapper;
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

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
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
    public CommandLineRunner run() {
        return runner -> {

//            CompetitionFamily fam = familyRepo.save(CompFamilyConstants.BUNDESLIGA);
//            Competition comp = compRepo.save(CompetitionConstants.getBundesliga2025(fam));
//            fam.addCompetition(comp);
//
//            CompetitionRound hinRunde = compRoundRepo.save(CompRoundConstants.getHinrunde(comp));
//            CompetitionRound rueckRunde = compRoundRepo.save(CompRoundConstants.getRueckrunde(comp));
//            comp.addCompetitionRound(hinRunde);
//            comp.addCompetitionRound(rueckRunde);
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
//            CompetitionTeam ct1 = new CompetitionTeam(bay, comp);
//            bay.addCompetitionTeam(ct1);
//            CompetitionTeam ct2 = new CompetitionTeam(hsv, comp);
//            hsv.addCompetitionTeam(ct2);
//            CompetitionTeam ct3 = new CompetitionTeam(pauli, comp);
//            pauli.addCompetitionTeam(ct3);
//            CompetitionTeam ct4 = new CompetitionTeam(werder, comp);
//            werder.addCompetitionTeam(ct4);
//            CompetitionTeam ct5 = new CompetitionTeam(bvb, comp);
//            bvb.addCompetitionTeam(ct5);
//            CompetitionTeam ct6 = new CompetitionTeam(koe, comp);
//            koe.addCompetitionTeam(ct6);
//            CompetitionTeam ct7 = new CompetitionTeam(wob, comp);
//            wob.addCompetitionTeam(ct7);
//            CompetitionTeam ct8 = new CompetitionTeam(lev, comp);
//            lev.addCompetitionTeam(ct8);
//            CompetitionTeam ct9 = new CompetitionTeam(vfb, comp);
//            vfb.addCompetitionTeam(ct9);
//            CompetitionTeam ct10 = new CompetitionTeam(freiburg, comp);
//            freiburg.addCompetitionTeam(ct10);
//            CompetitionTeam ct11 = new CompetitionTeam(frankfurt, comp);
//            frankfurt.addCompetitionTeam(ct11);
//            CompetitionTeam ct12 = new CompetitionTeam(hoffenheim, comp);
//            hoffenheim.addCompetitionTeam(ct12);
//            CompetitionTeam ct13 = new CompetitionTeam(augsburg, comp);
//            augsburg.addCompetitionTeam(ct13);
//            CompetitionTeam ct14 = new CompetitionTeam(union, comp);
//            union.addCompetitionTeam(ct14);
//            CompetitionTeam ct15 = new CompetitionTeam(mainz, comp);
//            mainz.addCompetitionTeam(ct15);
//            CompetitionTeam ct16 = new CompetitionTeam(heidenheim, comp);
//            heidenheim.addCompetitionTeam(ct16);
//            CompetitionTeam ct17 = new CompetitionTeam(leipzig, comp);
//            leipzig.addCompetitionTeam(ct17);
//            CompetitionTeam ct18 = new CompetitionTeam(gladbach, comp);
//            gladbach.addCompetitionTeam(ct18);
//
//            comp.addCompetitionTeam(ct1);
//            comp.addCompetitionTeam(ct2);
//            comp.addCompetitionTeam(ct3);
//            comp.addCompetitionTeam(ct4);
//
//            comp.addCompetitionTeam(ct5);
//            comp.addCompetitionTeam(ct6);
//            comp.addCompetitionTeam(ct7);
//            comp.addCompetitionTeam(ct8);
//
//            comp.addCompetitionTeam(ct9);
//            comp.addCompetitionTeam(ct10);
//            comp.addCompetitionTeam(ct11);
//            comp.addCompetitionTeam(ct12);
//
//            comp.addCompetitionTeam(ct13);
//            comp.addCompetitionTeam(ct14);
//            comp.addCompetitionTeam(ct15);
//            comp.addCompetitionTeam(ct16);
//            comp.addCompetitionTeam(ct17);
//            comp.addCompetitionTeam(ct18);
//
//
//            compTeamRepo.saveAll(List.of(ct1,ct2,ct3,ct4,ct5,ct6,ct7,ct8,ct9,ct10,ct11,ct12,ct13,ct14,ct15,ct16,ct17,ct18));
//
//            List<Spieltag> spieltagHin = spieltagRepo.saveAll(SpieltagConstants.getSpieltageHinrunde(hinRunde));
//
//            List<Spiel> spieleHin = spielRepo.saveAll(SpielConstants.getSpieleHinrunde(spieltagHin, pauli, hsv));
//
//
//            List<Spieltag> spieltagRueck = spieltagRepo.saveAll(SpieltagConstants.getSpieltageRueckrunde(rueckRunde));
//
//            List<Spiel> spieleRueck = spielRepo.saveAll(SpielConstants.getSpieleRückrunde(spieltagRueck, werder, bay));


            System.out.println("Save all cascade");
        };
    }
}

