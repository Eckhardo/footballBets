/*
 * Copyright (c) 2026. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package sportbets.persistence.rowObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * Represents  the whole picture table" for tipp result for tippers of a bet community
 */

public class TippsRow implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(TippsRow.class);

    //	********************** Fields ********************** //

    private Long spielId;
    private LocalDateTime anpfiffdate;

    private Integer heimTore;

    private Integer gastTore;
    private String username;

    private String heimName;

    private String gastName;
    private String roundName;
    private String groupName;
    private Long tippId;
    private Integer heimTipp;

    private Integer remisTipp;
    private Integer gastTipp;

    private Integer winPoints;

    private Integer sumWinPoints;


    //	********************** Constructors ********************** //
    public TippsRow() {

    }

    public TippsRow(Long spielId, LocalDateTime anpfiffDate, Integer heimtore,
                    Integer gasttore, String username, String heimName,
                    String gastName, String roundName) {
        this.spielId = spielId;
        this.anpfiffdate = anpfiffDate;
        this.heimTore = heimtore;
        this.gastTore = gasttore;
        this.username = username;
        this.heimName = heimName;
        this.gastName = gastName;
        this.roundName = roundName;
    }


    public TippsRow(Long spielId, LocalDateTime anpfiffDate, Integer heimtore,
                    Integer gasttore, String username, String heimName,
                    String gastName, String roundName, Long tippId, Integer heimTipp,
                    Integer remisTipp, Integer gastTipp, Integer winPoints) {
        this(spielId, anpfiffDate, heimtore, gasttore, username, heimName,
                gastName, roundName);
        this.tippId = tippId;
        this.heimTipp = heimTipp;
        this.remisTipp = remisTipp;
        this.gastTipp = gastTipp;
        this.winPoints = winPoints;

    }


    //	********************** Getter/Setter Methods ********************** //

    public Long getSpielId() {
        return spielId;
    }

    public LocalDateTime getAnpfiffdate() {
        return anpfiffdate;
    }

    public Integer getHeimTore() {
        return heimTore;
    }

    public Integer getGastTore() {
        return gastTore;
    }

    public String getUsername() {
        return username;
    }

    public String getHeimName() {
        return heimName;
    }

    public String getGastName() {
        return gastName;
    }

    public String getRoundName() {
        return roundName;
    }

    public String getGroupName() {
        return groupName;
    }

    public Long getTippId() {
        return tippId;
    }

    public Integer getHeimTipp() {
        return heimTipp;
    }

    public Integer getRemisTipp() {
        return remisTipp;
    }

    public Integer getGastTipp() {
        return gastTipp;
    }

    public Integer getWinPoints() {
        return winPoints;
    }

    public Integer getSumWinPoints() {
        return sumWinPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TippsRow tippsRow = (TippsRow) o;
        return Objects.equals(spielId, tippsRow.spielId) && Objects.equals(anpfiffdate, tippsRow.anpfiffdate) && Objects.equals(username, tippsRow.username) && Objects.equals(heimName, tippsRow.heimName) && Objects.equals(gastName, tippsRow.gastName) && Objects.equals(tippId, tippsRow.tippId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spielId, anpfiffdate, username, heimName, gastName, tippId);
    }

    @Override
    public String toString() {
        return "TippsRow{" +
                "spielId=" + spielId +
                ", anpfiffdate=" + anpfiffdate +
                ", heimTore=" + heimTore +
                ", gastTore=" + gastTore +
                ", username='" + username + '\'' +
                ", heimName='" + heimName + '\'' +
                ", gastName='" + gastName + '\'' +
                ", roundName='" + roundName + '\'' +
                ", groupName='" + groupName + '\'' +
                ", tippId=" + tippId +
                ", heimTipp=" + heimTipp +
                ", remisTipp=" + remisTipp +
                ", gastTipp=" + gastTipp +
                ", winPoints=" + winPoints +
                ", sumWinPoints=" + sumWinPoints +
                '}';
    }
}