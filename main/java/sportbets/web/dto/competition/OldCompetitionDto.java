package sportbets.web.dto.competition;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import sportbets.persistence.entity.competition.Competition;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link Competition}
 */

@Deprecated
@Schema(deprecated = true)
public class OldCompetitionDto implements Serializable {
    private Long id;
    @NotBlank(message = "name must not be empty")
    private String name;
    @Range(min = 1, max = 9, message = "Win Multiplicator  must be single-digit number")
    private int winMultiplicator;
    @Range(min = 1, max = 9, message = "Remis Multiplicator  must be single-digit number")
    private int remisMultiplicator;
    @NotNull(message = " family id cannot be null")
    private Long familyId;
    private String familyName;

    public OldCompetitionDto() {
    }

    public OldCompetitionDto(Long id, String name, int winMultiplicator, int remisMultiplicator, Long familyId, String familyName) {
        this.id = id;
        this.name = name;
        this.winMultiplicator = winMultiplicator;
        this.remisMultiplicator = remisMultiplicator;
        this.familyId = familyId;
        this.familyName = familyName;


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWinMultiplicator() {
        return winMultiplicator;
    }

    public void setWinMultiplicator(int winMultiplicator) {
        this.winMultiplicator = winMultiplicator;
    }

    public int getRemisMultiplicator() {
        return remisMultiplicator;
    }

    public void setRemisMultiplicator(int remisMultiplicator) {
        this.remisMultiplicator = remisMultiplicator;
    }


    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OldCompetitionDto entity = (OldCompetitionDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.winMultiplicator, entity.winMultiplicator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, winMultiplicator, remisMultiplicator);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "familyId = " + familyId + ", " +
                "familyName = " + familyName + ", " +
                "winMultiplicator = " + winMultiplicator + ", " +
                "remisMultiplicator = " + remisMultiplicator + ")";
    }
}