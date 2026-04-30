package sportbets.persistence.rowObject;

public class CompRow {

    private final String name;
    private final String description;
    private final Long familyId;

    public CompRow(String name, String description, Long familyId) {
        this.name = name;
        this.description = description;
        this.familyId = familyId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getFamilyId() {
        return familyId;
    }

    @Override
    public String toString() {
        return "CompRow{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", familyId=" + familyId +
                '}';
    }
}
