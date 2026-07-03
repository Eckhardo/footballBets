/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.web.dto.tipps;

import sportbets.persistence.entity.tipps.enums.TippModusType;
@Deprecated
public class TippModusTotoDescription implements TippModusIF {

    public TippModusTotoDescription() {

    }
    @Override
    public String getType() {
        return TippModusType.TIPPMODUS_TOTO.getDisplayName();
    }
    @Override
    public String getName() {
        return " Toto Tipp";
    }
    @Override
    public String getDescription() {
        return """
                \
                1 = Sieg der Heimmannschaft (erstgenannter Verein)\s
                0 = Unentschieden\s
                2 = Sieg der Gastmannschaft (zweitgenannter Verein)""";
    }
}
