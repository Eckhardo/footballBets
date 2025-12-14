package sportbets.persistence.builder;

import sportbets.persistence.entity.CompetitionRound;
import sportbets.persistence.entity.Spieltag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SpieltagConstants {
    public static List<Spieltag> SPIELTAGE = new ArrayList<>();

    public static List<Spieltag> getSpieltageHinrunde(CompetitionRound round) {
        SPIELTAGE.clear();
        for (int i = 1; i <= 17; i++) {

            SPIELTAGE.add(new Spieltag(i, LocalDateTime.now(), round));
        }
        SPIELTAGE.forEach(round::addSpieltag);
        return SPIELTAGE;
    }

    public static List<Spieltag> getSpieltageRueckrunde(CompetitionRound round) {
        SPIELTAGE.clear();
        for (int j = 18; j <= 34; j++) {

            SPIELTAGE.add(new Spieltag(j, LocalDateTime.now(), round));
        }
        SPIELTAGE.forEach(round::addSpieltag);
        return SPIELTAGE;
    }
}