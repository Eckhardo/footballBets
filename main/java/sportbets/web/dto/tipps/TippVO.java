/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.web.dto.tipps;

public record TippVO(Long compId, Long roundId,Long spieltagId,Long commId, Long commMembId, Integer startSpieltag, Integer stopSpieltag) {

    public TippVO(Long compId, Long roundId, Long spieltagId, Long commId, Long commMembId, Integer startSpieltag, Integer stopSpieltag) {
        this.compId = compId;
        this.roundId = roundId;
        this.spieltagId = spieltagId;
        this.commId = commId;
        this.commMembId = commMembId;
        this.startSpieltag = startSpieltag;
        this.stopSpieltag = stopSpieltag;
    }


}
