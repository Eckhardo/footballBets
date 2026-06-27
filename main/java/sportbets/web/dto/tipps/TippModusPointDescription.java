/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.web.dto.tipps;

import sportbets.persistence.entity.tipps.enums.TippModusType;

public class TippModusPointDescription implements TippModusIF {

    public TippModusPointDescription() {
    }

    @Override
    public String getType() {
        return TippModusType.TIPPMODUS_POINT.getDisplayName();
    }

    @Override
    public String getName() {
        return "Punkte Tipp";
    }
    @Override
    public String getDescription() {
        return "Verteilungspunkte: Sie bestimmen eine Anzahl von Punkten, die fei nach Wahl auf Heimsieg, Auswärtssieg oder unentschieden verteilt werden. " +
                "Ist die Anzahl der Verteilungspunkte z. B. auf 4 festgelegt, können diese 4 Punkte in allen möglichen Variationen verteilt werden( z.B. 4:0:0, 0:0:4 oder auch 1:0:3 oder 2:2:0 ";
    }
}
