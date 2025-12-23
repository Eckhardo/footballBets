package sportbets.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import sportbets.persistence.entity.Competition;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link Competition}
 */
public class CompetitionDto implements Serializable {
    private Long id;
    @NotBlank(message = "name must not be empty")
    private  String name;
    @Size(min = 10, max = 50,
            message = "description must be between 10 and 50 characters long")
    private  String description;
    @Range(min = 1, max =9,message = "Win Multiplicator  must be single-digit number")
    private  int winMultiplicator;
    @Range(min = 1, max = 9, message = "Remis Multiplicator  must be single-digit number")
    private  int remisMultiplicator;

    @NotNull(message = " family id cannot be null")
    private Long familyId;
    private String familyName;

    public CompetitionDto() {
    }

    public CompetitionDto(Long id, String name, String description, int winMultiplicator, int remisMultiplicator, Long familyId, String familyName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.winMultiplicator = winMultiplicator;
        this.remisMultiplicator = remisMultiplicator;
        this.familyId = familyId;
        this.familyName= familyName;


    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getWinMultiplicator() {
        return winMultiplicator;
    }

    public int getRemisMultiplicator() {
        return remisMultiplicator;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWinMultiplicator(int winMultiplicator) {
        this.winMultiplicator = winMultiplicator;
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
        CompetitionDto entity = (CompetitionDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.winMultiplicator, entity.winMultiplicator) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, winMultiplicator, remisMultiplicator);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "familyId = " + familyId + ", " +
                "familyName = " + familyName + ", " +
                "winMultiplicator = " + winMultiplicator + ", " +
                "remisMultiplicator = " + remisMultiplicator +")";
    }
}