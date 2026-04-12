package sportbets.persistence.entity.tipps;

import jakarta.persistence.*;

@Entity
@Table(name = "tipp")
public class Tipp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}