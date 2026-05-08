package sportbets.persistence.rowObject;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;


public class TippRow  {
    private static final Logger log = LoggerFactory.getLogger(TippRow.class);
    //	********************** Fields ********************** //



    private Long spielId;


    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    // Specifies the format for JSON serialization (when the entity is returned as a response)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime anpfiffdate;


    private Integer heimTore;
    private Integer gastTore;
    private Boolean hasStattgefunden;;

    private String heimName;
    private String gastName;

    private Integer spieltagNumber;
    private String roundName;
    private String competitionName;

    private String groupName;

    // tipp fields

    private Long tippId;
    private Integer heimTipp;
    private Integer remisTipp;
    private Integer gastTipp;

    private Integer winPoints;

    private Long commMembId;

    private Integer totoTippInput;

    //	********************** Constructors ********************** //

    /**
     * NOT placed tipps yet !
     */

    public TippRow(Long spielId, LocalDateTime anpfiffdate, Integer heimtore,
                   Integer gasttore, boolean stattgefunden, String heimName,
                   String gastName, Integer spieltagNumber, String roundName,
                   String competitionName) {
        this.spielId = spielId;
        this.anpfiffdate = anpfiffdate;
        this.heimTore = heimtore;
        this.gastTore = gasttore;
        this.hasStattgefunden = stattgefunden;
        this.heimName = heimName;
        this.gastName = gastName;
        this.spieltagNumber = spieltagNumber;
        this.roundName = roundName;
        this.competitionName = competitionName;
    }

    /**
     * NOT placed tipps yet !
     *
     */

    public TippRow(Long spielId, LocalDateTime anpfiffdate, Integer heimtore,
                   Integer gasttore, Boolean stattgefunden, String heimName,
                   String gastName, Integer spieltagNumber, String roundName,
                   String competitionName, String groupName) {
        this(spielId, anpfiffdate, heimtore, gasttore, stattgefunden, heimName,
                gastName, spieltagNumber, roundName, competitionName);
        this.groupName = groupName;

    }

    /**
     * FOR UPDATE TIPPS & VIEW TIPPS
     *
     * Full constructor for single tipper tipps for comp without groups
     *
     */
    public TippRow(Long spielId, LocalDateTime anpfiffdate, Integer heimtore,
                   Integer gasttore, Boolean stattgefunden, String heimName,
                   String gastName, Integer spieltagNumber, String roundName,
                   String competitionName, Long tippId, Integer heimTipp,
                   Integer remisTipp, Integer gastTipp, Integer winPoints,
                   Long commMembId) {
        this(spielId, anpfiffdate, heimtore, gasttore, stattgefunden, heimName,
                gastName, spieltagNumber, roundName, competitionName);
        this.tippId = tippId;
        this.heimTipp = heimTipp;
        this.remisTipp = remisTipp;
        this.gastTipp = gastTipp;
        this.winPoints = winPoints;
        this.commMembId = commMembId;

    }

    /**
     * FOR UPDATE TIPPS & VIEW TIPPS
     *
     * Full constructor for single tipper tipps for comp with groups
     */
    public TippRow(Long spielId, LocalDateTime anpfiffdate, Integer heimtore,
                   Integer gasttore, Boolean stattgefunden, String heimName,
                   String gastName, Integer spieltagNumber, String roundName,
                   String competitionName, String groupName, Long tippId,
                   Integer heimTipp, Integer remisTipp, Integer gastTipp,
                   Integer winPoints, Long commMembId) {
        this(spielId, anpfiffdate, heimtore, gasttore, stattgefunden, heimName,
                gastName, spieltagNumber, roundName, competitionName, groupName);
        this.tippId = tippId;
        this.heimTipp = heimTipp;
        this.remisTipp = remisTipp;
        this.gastTipp = gastTipp;
        this.winPoints = winPoints;
        this.commMembId = commMembId;

    }

    //	********************** Getter/Setter Methods ********************** //

    public Long getSpielId() {
        return spielId;
    }

    public void setSpielId(Long spielId) {
        this.spielId = spielId;
    }

    public LocalDateTime getAnpfiffdate() {
        return anpfiffdate;
    }

    public void setAnpfiffdate(LocalDateTime anpfiffdate) {
        this.anpfiffdate = anpfiffdate;
    }

    public Integer getHeimTore() {
        return heimTore;
    }

    public void setHeimTore(Integer heimTore) {
        this.heimTore = heimTore;
    }

    public Integer getGastTore() {
        return gastTore;
    }

    public void setGastTore(Integer gastTore) {
        this.gastTore = gastTore;
    }

    public Boolean getHasStattgefunden() {
        return hasStattgefunden;
    }

    public void setHasStattgefunden(Boolean hasStattgefunden) {
        this.hasStattgefunden = hasStattgefunden;
    }

    public String getHeimName() {
        return heimName;
    }

    public void setHeimName(String heimName) {
        this.heimName = heimName;
    }

    public String getGastName() {
        return gastName;
    }

    public void setGastName(String gastName) {
        this.gastName = gastName;
    }

    public Integer getSpieltagNumber() {
        return spieltagNumber;
    }

    public void setSpieltagNumber(Integer spieltagNumber) {
        this.spieltagNumber = spieltagNumber;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getTippId() {
        return tippId;
    }

    public void setTippId(Long tippId) {
        this.tippId = tippId;
    }

    public Integer getHeimTipp() {
        return heimTipp;
    }

    public void setHeimTipp(Integer heimTipp) {
        this.heimTipp = heimTipp;
    }

    public Integer getRemisTipp() {
        return remisTipp;
    }

    public void setRemisTipp(Integer remisTipp) {
        this.remisTipp = remisTipp;
    }

    public Integer getGastTipp() {
        return gastTipp;
    }

    public void setGastTipp(Integer gastTipp) {
        this.gastTipp = gastTipp;
    }

    public Integer getWinPoints() {
        return winPoints;
    }

    public void setWinPoints(Integer winPoints) {
        this.winPoints = winPoints;
    }

    public Long getCommMembId() {
        return commMembId;
    }

    public void setCommMembId(Long commMembId) {
        this.commMembId = commMembId;
    }

    //	********************** Business Methods ********************** //

    /**
     * @return Returns the totoTippInput.
     */
    public Integer getTotoTippInput() {
        if (heimTipp != null && heimTipp == 1) {
            return 1;
        } else if (remisTipp != null && remisTipp == 1) {
            return 0;
        } else if (gastTipp != null && gastTipp == 1) {
            return 2;
        } else {
            return null;
        }

    }

    /**
     * @param totoTippInput
     *            The totoTippInput to set.
     */
    public void setTotoTippInput(Integer totoTippInput) {
        if (totoTippInput != null && totoTippInput == 1) {
            heimTipp = 1;
            remisTipp = 0;
            gastTipp = 0;

        } else if (totoTippInput != null && totoTippInput== 0) {
            heimTipp = 0;
            remisTipp = 1;
            gastTipp = 0;
        } else if (totoTippInput != null && totoTippInput == 2) {
            heimTipp = 0;
            remisTipp = 0;
            gastTipp = 1;
        } else {
            heimTipp = 0;
            remisTipp = 0;
            gastTipp = 0;
        }

    }
}
