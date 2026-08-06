/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.web.dto.tipps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.rowObject.TippRow;

import java.io.Serializable;
import java.util.List;

public class TippsContainerDto implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(TippsContainerDto.class);
    private  List<TippRow> tippRows;
    private  boolean isUpdate;
    private  Long commMembId;
    private Long matchdayId;

    public TippsContainerDto() {
    }

    public TippsContainerDto(List<TippRow> tippRows, boolean isUpdate, Long commMembId, Long matchdayId) {
        this.tippRows = tippRows;
        this.isUpdate = isUpdate;
        this.commMembId = commMembId;
        this.matchdayId = matchdayId;
    }

    public List<TippRow> getTippRows() {
        return tippRows;
    }

    public void setTippRows(List<TippRow> tippRows) {
        this.tippRows = tippRows;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public Long getCommMembId() {
        return commMembId;
    }

    public void setCommMembId(Long commMembId) {
        this.commMembId = commMembId;
    }

    public Long getMatchdayId() {
        return matchdayId;
    }

    public void setMatchdayId(Long matchdayId) {
        this.matchdayId = matchdayId;
    }
}
