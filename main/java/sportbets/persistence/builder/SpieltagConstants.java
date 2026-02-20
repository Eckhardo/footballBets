package sportbets.persistence.builder;

import sportbets.persistence.entity.competition.CompetitionRound;
import sportbets.persistence.entity.competition.Spieltag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class SpieltagConstants {
    public static final List<Spieltag> SPIELTAGE = new ArrayList<>();

    public static List<Spieltag> getSpieltageHinrunde(CompetitionRound round, List<LocalDateTime> hinDates) {
        SPIELTAGE.clear();
        for (int i = 1; i <= 17; i++) {

            SPIELTAGE.add(new Spieltag(i, hinDates.get(i - 1), round));
        }
        SPIELTAGE.forEach(round::addSpieltag);
        return SPIELTAGE;
    }

    public static List<Spieltag> getSpieltageRueckrunde(CompetitionRound round, List<LocalDateTime> rueckDates) {
        SPIELTAGE.clear();
        for (int j = 18, i = 1; j <= 34; j++, i++) {

            SPIELTAGE.add(new Spieltag(j, rueckDates.get(i - 1), round));
        }
        SPIELTAGE.forEach(round::addSpieltag);
        return SPIELTAGE;
    }
}