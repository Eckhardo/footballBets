package sportbets.persistence.entity.authorization;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_types", discriminatorType = DiscriminatorType.STRING)

public abstract class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    private final LocalDateTime createdOn = LocalDateTime.now();

    public Role() {
    }

    /**
     * Simple properties constructor;
     */
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
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

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role rolle = (Role) o;
        return Objects.equals(name, rolle.name) && Objects.equals(description, rolle.description) && Objects.equals(createdOn, rolle.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, createdOn);
    }
}