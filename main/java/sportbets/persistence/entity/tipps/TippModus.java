package sportbets.persistence.entity.tipps;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import sportbets.common.TippModi;
import sportbets.common.TippModiAttributeConverter;
import sportbets.persistence.entity.community.Community;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TippModus implements Serializable, Comparable<TippModus> {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Convert(converter = TippModiAttributeConverter.class)
    private TippModi name;

    /**
     * an Integer representation for the time when tipps have to be placed
     * before the speiltag begind
     */
    private Integer deadline;

    private String displayName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_comm_id",foreignKey = @ForeignKey(name = "FK_TIPP_MODUS_TO_COMMUNITY"))
    @NotNull
    private Community community;

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
        super();
    }

    /**
     * Simple properties constructor .
     */
    public TippModus(TippModi name, Integer deadline, Community community) {
        super();
        this.name = name;
        this.deadline = deadline;
        this.community = community;
        community.addTippModus(this);
    }

    /**
     * Full constructor .
     */
    public TippModus(TippModi name, Integer deadline, Community community,
                     Set<TippConfig> tippConfigs) {
        this(name, deadline, community);
        this.tippConfigs = tippConfigs;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TippModi getName() {
        return name;
    }

    public void setName(TippModi name) {
        this.name = name;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    @Override
    public int compareTo(TippModus other) {
        if (this.name == null || other.getName() == null) {
            return 0; // Oder andere Logik für unvollständige Daten
        }
        return this.name.compareTo(other.getName());
    }

    public Set<TippConfig> getTippConfigs() {
        return tippConfigs;
    }

    public void addTippConfig(TippConfig tippConfig) {
        this.tippConfigs.add(tippConfig);
    }
}