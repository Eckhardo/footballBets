package sportbets.web.dto.competition;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class SpielDto implements Serializable, Comparable<SpielDto> {

    private Long id;
    @NotNull(message = " spiel number cannot be null")
    private int spielNumber;
    @NotNull(message = " heimTore cannot be null")
    private int heimTore;
    @NotNull(message = " gastTore cannot be null")
    private int gastTore;

    private boolean stattgefunden;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    // Specifies the format for JSON serialization (when the entity is returned as a response)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime anpfiffdate;


    @NotNull
    private Long spieltagId;
    @NotNull
    private int spieltagNumber;

    private Long heimTeamId;
    private String heimTeamAcronym;

    private Long gastTeamId;
    private String gastTeamAcronym;

    public SpielDto() {
    }

    public SpielDto(Long id, int spielNumber, int heimTore, int gastTore, boolean stattgefunden, LocalDateTime anpfiffdate, Long spieltagId, int spieltagNumber, Long heimTeamId, String heimTeamAcronym, Long gastTeamId, String gastTeamAcronym) {
        this.id = id;
        this.spielNumber = spielNumber;
        this.heimTore = heimTore;
        this.gastTore = gastTore;
        this.stattgefunden = stattgefunden;
        this.anpfiffdate = anpfiffdate;
        this.spieltagId = spieltagId;
        this.spieltagNumber = spieltagNumber;
        this.heimTeamId = heimTeamId;
        this.heimTeamAcronym = heimTeamAcronym;
        this.gastTeamId = gastTeamId;
        this.gastTeamAcronym = gastTeamAcronym;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSpielNumber() {
        return spielNumber;
    }

    public void setSpielNumber(int spielNumber) {
        this.spielNumber = spielNumber;
    }

    public int getHeimTore() {
        return heimTore;
    }

    public void setHeimTore(int heimTore) {
        this.heimTore = heimTore;
    }

    public int getGastTore() {
        return gastTore;
    }

    public void setGastTore(int gastTore) {
        this.gastTore = gastTore;
    }

    public boolean isStattgefunden() {
        return stattgefunden;
    }

    public void setStattgefunden(boolean stattgefunden) {
        this.stattgefunden = stattgefunden;
    }

    public LocalDateTime getAnpfiffdate() {
        return anpfiffdate;
    }

    public void setAnpfiffdate(LocalDateTime anpfiffdate) {
        this.anpfiffdate = anpfiffdate;
    }

    public Long getSpieltagId() {
        return spieltagId;
    }

    public void setSpieltagId(Long spieltagId) {
        this.spieltagId = spieltagId;
    }

    public int getSpieltagNumber() {
        return spieltagNumber;
    }

    public void setSpieltagNumber(int spieltagNumber) {
        this.spieltagNumber = spieltagNumber;
    }

    public Long getHeimTeamId() {
        return heimTeamId;
    }

    public void setHeimTeamId(Long heimTeamId) {
        this.heimTeamId = heimTeamId;
    }

    public String getHeimTeamAcronym() {
        return heimTeamAcronym;
    }

    public void setHeimTeamAcronym(String heimTeamAcronym) {
        this.heimTeamAcronym = heimTeamAcronym;
    }

    public Long getGastTeamId() {
        return gastTeamId;
    }

    public void setGastTeamId(Long gastTeamId) {
        this.gastTeamId = gastTeamId;
    }

    public String getGastTeamAcronym() {
        return gastTeamAcronym;
    }

    public void setGastTeamAcronym(String gastTeamAcronym) {
        this.gastTeamAcronym = gastTeamAcronym;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SpielDto spielDto = (SpielDto) o;
        return spielNumber == spielDto.spielNumber && heimTore == spielDto.heimTore && gastTore == spielDto.gastTore && Objects.equals(anpfiffdate, spielDto.anpfiffdate) && Objects.equals(spieltagId, spielDto.spieltagId) && Objects.equals(heimTeamId, spielDto.heimTeamId) && Objects.equals(gastTeamId, spielDto.gastTeamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spielNumber, heimTore, gastTore, anpfiffdate, spieltagId, heimTeamId, gastTeamId);
    }

    @Override
    public String toString() {
        return "SpielDto{" +
                "spielNumber=" + spielNumber +
                ", heimTore=" + heimTore +
                ", gastTore=" + gastTore +
                ", spieltagId=" + spieltagId +
                ", spieltag number=" + spieltagNumber +
                ", heimTeamId=" + heimTeamId +
                ", gastTeamId=" + gastTeamId +
                ", heimTeam name=" + heimTeamAcronym +
                ", gastTeam name=" + gastTeamAcronym +
                '}';
    }

    /**
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(SpielDto o) {
        return this.getSpielNumber() - o.getSpielNumber();
    }
}
