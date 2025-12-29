package sportbets.persistence.entity.community;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sportbets.persistence.entity.authorization.TipperRole;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tipper")
public class Tipper {
    private static final Logger log = LoggerFactory.getLogger(Tipper.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @Column(unique = true, nullable = false)
    private String username;
    @NotNull
    private String passwort;
    private String passwortHint;
    @Email
    private String email;
    private Long defaultCompetitionId;

    @OneToMany(mappedBy = "tipper", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    final Set<TipperRole> tipperRoles = new HashSet<>();

    public Tipper() {
    }

    public Tipper(String firstname, String lastname, String username, String passwort, String passwortHint, String email, Long defaultCompetitionId) {
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

    public Set<TipperRole> getTipperRoles() {
        return tipperRoles;
    }

    public void addTipperRole(TipperRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Can't add a null tipper role.");
        }
        this.getTipperRoles().add(role);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tipper tipper = (Tipper) o;
        return Objects.equals(firstname, tipper.firstname) && Objects.equals(lastname, tipper.lastname) && Objects.equals(username, tipper.username) && Objects.equals(passwort, tipper.passwort) && Objects.equals(email, tipper.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, username, passwort, email);
    }

    @Override
    public String toString() {
        return "Tipper{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", defaultCompetitionId=" + defaultCompetitionId +
                '}';
    }
}