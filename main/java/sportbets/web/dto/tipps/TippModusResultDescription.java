/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.web.dto.tipps;

import sportbets.persistence.entity.tipps.enums.TippModusType;

public class TippModusResultDescription implements TippModusIF {

    public TippModusResultDescription() {
    }
    @Override
    public String getType() {
        return TippModusType.TIPPMODUS_RESULT.getDisplayName();
    }
    @Override
    public String getName() {
        return " Ergebnis Tipp";
    }
    @Override
    public String getDescription() {
        return "Tendenz: Sie tippen lediglich, welches Team gewinnt (Heimsieg, Auswärtssieg) oder ob es unentschieden ausgeht. \n " +
                "Bonus für das exakte Ergbnis:\n" +
                "Der Tipp stimmt exakt mit dem tatsächlichen Spielergebnis überein (Bsp: Sie tippen 2:1, das Spiel endet 2:1). ";
    }
}
