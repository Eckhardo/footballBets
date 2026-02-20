package sportbets.persistence.entity.authorization;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.competition.Competition;

import java.util.Objects;

@Entity
@DiscriminatorValue("COMP")
public class CompetitionRole extends Role {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_comp_id",foreignKey = @ForeignKey(name = "FK_COMP_ROLE_TO_COMP"))
    @NotNull
    public Competition competition;


    public CompetitionRole() {
    }

    public CompetitionRole(String name, String description,
                           Competition competition) {
        super(name, description);
        this.competition = competition;
    }

    public Competition getCompetition() {
        return this.competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    @Override
    public String toString() {
        return super.toString() + "CompetitionRole{" +
                "competition=" + competition.getName() +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CompetitionRole that = (CompetitionRole) o;
        return Objects.equals(competition, that.competition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(),getDescription());
    }
}