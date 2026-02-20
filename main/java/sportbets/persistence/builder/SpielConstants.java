package sportbets.persistence.builder;

import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.entity.competition.Team;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SpielConstants {
    public static final List<Spiel> SPIELE = new ArrayList<>();

    public static List<Spiel> getSpieleHinrunde(List<Spieltag> spieltage, Team heim, Team gast) {
        SPIELE.clear();
        for (int i = 0; i < spieltage.size(); i++) {

            final Spieltag sp = spieltage.get(i);
            for (int j = 1; j <= 9; j++) {

                SPIELE.add(new Spiel(sp, j, LocalDateTime.now(),
                        heim, gast));
            }
        }
        SPIELE.forEach(spiel -> {
            Spieltag s = spiel.getSpieltag();
            s.addSpiel(spiel);
        });
        return SPIELE;
    }

    public static List<Spiel> getSpieleRückrunde(List<Spieltag> spieltage, Team heim, Team gast) {
        SPIELE.clear();
        for (int i = 0; i < spieltage.size(); i++) {

            Spieltag sp = spieltage.get(i);
            for (int j = 1; j <= 9; j++) {

                SPIELE.add(new Spiel(sp, j, LocalDateTime.now(),
                        heim, gast));
            }
        }
        SPIELE.forEach(spiel -> {
            Spieltag s = spiel.getSpieltag();
            s.addSpiel(spiel);
        });
        return SPIELE;
    }
}

