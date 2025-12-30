package sportbets.web.dto.community;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sportbets.persistence.entity.community.Community;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link Community}
 */
public class CommunityDto implements Serializable {
    private Long id;
    @Size(message = "Name must be at least 5 chars long", min = 5, max = 20)
    private String name;
    @NotNull
    @NotEmpty(message = "must not be empty")
    private String description;
    private LocalDateTime createdOn = LocalDateTime.now();

    public CommunityDto() {
    }

    public CommunityDto(Long id, String name, String description, LocalDateTime createdOn) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdOn = createdOn;
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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunityDto entity = (CommunityDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.createdOn, entity.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, createdOn);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "createdOn = " + createdOn + ")";
    }
}