package sportbets.web.dto;

import sportbets.persistence.entity.CompetitionFamily;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * DTO for {@link CompetitionFamily}
 */
public class CompetitionFamilyDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Date createdOn = new Date();
    private boolean hasLigaModus;
    private boolean hasClubs;

    public CompetitionFamilyDto() {
    }

    public CompetitionFamilyDto(Long id, String name, String description, Date createdOn, boolean hasLigaModus, boolean hasClubs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdOn = createdOn;
        this.hasLigaModus = hasLigaModus;
        this.hasClubs = hasClubs;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public boolean getHasLigaModus() {
        return hasLigaModus;
    }

    public void setHasLigaModus(boolean hasLigaModus) {
        this.hasLigaModus = hasLigaModus;
    }

    public boolean getHasClubs() {
        return hasClubs;
    }

    public void setHasClubs(boolean hasClubs) {
        this.hasClubs = hasClubs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionFamilyDto entity = (CompetitionFamilyDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.createdOn, entity.createdOn) &&
                Objects.equals(this.hasLigaModus, entity.hasLigaModus) &&
                Objects.equals(this.hasClubs, entity.hasClubs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, createdOn, hasLigaModus, hasClubs);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "createdOn = " + createdOn + ", " +
                "hasLigaModus = " + hasLigaModus + ", " +
                "hasClubs = " + hasClubs + ")";
    }
}