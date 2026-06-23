package sportbets.persistence.builder;

import sportbets.persistence.entity.community.Tipper;
import sportbets.web.dto.community.TipperDto;

public class TipperConstants {

    public static Tipper getECKHARD() {
        return new Tipper("Eckhard", "Kirschning", "Eckhardo", "hrubesch", "ungeheuer", "taerna@gmx.de", true, true);

    }

    public static Tipper getBERND() {
        return   new Tipper("Bernd", "Ahlers", "Bernd", "hrubesch", "ungeheuer", "bernd@gmx.de");

    }

    public static Tipper getDIERK() {
        return   new Tipper("Dierk", "Ahlers", "Dierk", "hrubesch", "ungeheuer", "dierk@gmx.de");

    }


    public static TipperDto createValidTipperDto(){
       return   new TipperDto(null, "Werner", "Wernersen", "Wernerdo", "banane", "frucht", "werner@gmx.de", null);

    }

    public static TipperDto createValidTipperDto2(){
        return   new TipperDto(null, "Otto", "Ottosen", "ottoso", "banane", "frucht", "werner@gmx.de", null);

    }

    public static Tipper createValidTipper(){
        return   new Tipper("Werner", "Wernersen", "Wernerdo", "hrubesch", "ungeheuer", "werner@gmx.de");

    }

}
