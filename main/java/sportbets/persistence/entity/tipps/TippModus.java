package sportbets.persistence.entity.tipps;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import sportbets.persistence.entity.community.Community;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.tipps.converter.TippModusTypeAttributeConverter;
import sportbets.persistence.entity.tipps.enums.TippModusType;
import sportbets.web.dto.tipps.TippDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TippModus {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String name;
    @Convert(converter = TippModusTypeAttributeConverter.class)
    private TippModusType type;

    /**
     * an Integer representation for the time when tipps have to be placed
     * before the speiltag begind
     */
    private Integer deadline;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_comm_id", foreignKey = @ForeignKey(name = "FK_TIPP_MODUS_TO_COMMUNITY"))
    @NotNull
    private Community community;


    @OneToMany(mappedBy = "tippModus", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    Set<Tipp> tipps = new HashSet<>();

    @OneToMany(mappedBy = "tippModus", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TippConfig> tippConfigs = new HashSet();


    @Column(nullable = false)
    private final LocalDateTime createdOn = LocalDateTime.now();

    protected static final int HOMEWIN = 1;

    protected static final int DRAW = 0;

    protected static final int GUESTWIN = 2;

    /**
     * No-arg constructor for JavaBean tools.
     */
    public TippModus() {

    }

    /**
     * Simple properties constructor .
     */
    public TippModus(String name, TippModusType type, Integer deadline, Community community) {
        super();
        this.name = name;
        this.type = type;
        this.deadline = deadline;
        this.community = community;
        community.addTippModus(this);
    }

    /**
     * Full constructor .
     */
    public TippModus(String name, TippModusType type, Integer deadline, Community community,
                     Set<TippConfig> tippConfigs) {
        this(name, type, deadline, community);
        this.tippConfigs = tippConfigs;
    }
//--------------- abstract methods ----------------------------------
    public abstract boolean isTippValid(@NotNull TippDto tipp);

    public abstract int calculateWinPoints(TippDto tipp, Spiel spiel);



    //-------------- getter and setter-------------------------------
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

    public TippModusType getType() {
        return type;
    }

    public void setType(TippModusType name) {
        this.type = name;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public String getDisplayName() {
        return this.type.getDisplayName();
    }


    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Set<Tipp> getTipps() {
        return tipps;
    }

    public void addTipp(Tipp tipp) {
        this.tipps.add(tipp);
    }
    public Set<TippConfig> getTippConfigs() {
        return tippConfigs;
    }

    public void addTippConfig(TippConfig tippConfig) {
        this.tippConfigs.add(tippConfig);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TippModus tippModus = (TippModus) o;
        return Objects.equals(name, tippModus.name) && type == tippModus.type && Objects.equals(createdOn, tippModus.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, createdOn);
    }

    @Override
    public String toString() {
        return "TippModus{" +
                "id=" + id +
                ", name=" + name +
                ", type=" + type +
                ", deadline=" + deadline +
                ", createdOn=" + createdOn +
                ", community=" + community +
                '}';
    }
}