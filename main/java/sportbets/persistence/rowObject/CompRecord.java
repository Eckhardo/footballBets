package sportbets.persistence.rowObject;

public class CompRecord {

  private final  String name;
  private final String description;
  private  final Long familyId;

    public CompRecord(String name, String description, Long familyId) {
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
        return "CompRecord{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", familyId=" + familyId +
                '}';
    }
}
