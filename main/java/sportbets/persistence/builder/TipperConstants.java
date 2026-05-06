package sportbets.persistence.builder;

import sportbets.persistence.entity.community.Tipper;
import sportbets.web.dto.community.TipperDto;

public class TipperConstants {

    public static TipperDto ECKHARD_DTO = new TipperDto(null,"Eckhard", "Kirschning", "Eckhardo", "hrubesch", "ungeheuer", "taerna@gmx.de");

    public static Tipper ECKHARD = new Tipper("Eckhard", "Kirschning", "Eckhardo", "hrubesch", "ungeheuer", "taerna@gmx.de", true,  true);

    public static Tipper WERNER = new Tipper("Werner", "Wernersen", "Wernerdo", "hrubesch", "ungeheuer", "werner@gmx.de");
    public static TipperDto WERNER_DTO = new TipperDto(null,"Werner", "Wernersen", "Wernerdo", "banane", "frucht", "werner@gmx.de");


}
