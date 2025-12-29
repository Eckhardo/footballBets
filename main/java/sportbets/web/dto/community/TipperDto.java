package sportbets.web.dto.community;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import sportbets.persistence.entity.community.Tipper;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link Tipper}
 */
public class TipperDto implements Serializable {
    private Long id;
    private String firstname;
    private String lastname;
    @NotNull
    private String username;
    @NotNull
    @Length(min = 5, max = 10, message = " password must have at least 3 chars and up to 10 chars")
    private String passwort;

    private String passwortHint;
    @NotNull
    @Email(message = " invalid email")
    private String email;

    private Long defaultCompetitionId;

    public TipperDto() {
    }

    public TipperDto(Long id, String firstname, String lastname, String username, String passwort, String passwortHint, String email, Long defaultCompetitionId) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.passwort = passwort;
        this.passwortHint = passwortHint;
        this.email = email;
        this.defaultCompetitionId = defaultCompetitionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getPasswortHint() {
        return passwortHint;
    }

    public void setPasswortHint(String passwortHint) {
        this.passwortHint = passwortHint;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDefaultCompetitionId() {
        return defaultCompetitionId;
    }

    public void setDefaultCompetitionId(Long defaultCompetitionId) {
        this.defaultCompetitionId = defaultCompetitionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipperDto entity = (TipperDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.firstname, entity.firstname) &&
                Objects.equals(this.lastname, entity.lastname) &&
                Objects.equals(this.username, entity.username) &&
                Objects.equals(this.passwort, entity.passwort) &&
                Objects.equals(this.passwortHint, entity.passwortHint) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.defaultCompetitionId, entity.defaultCompetitionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, username, passwort, passwortHint, email, defaultCompetitionId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "firstname = " + firstname + ", " +
                "lastname = " + lastname + ", " +
                "username = " + username + ", " +
                "passwort = " + passwort + ", " +
                "passwortHint = " + passwortHint + ", " +
                "email = " + email + ", " +
                "defaultCompetitionId = " + defaultCompetitionId + ")";
    }
}