package sportbets.persistence.repository.authorization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.authorization.CommunityRole;
import sportbets.persistence.entity.authorization.CompetitionRole;
import sportbets.persistence.entity.authorization.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select  r from Role r where  TYPE(r) = CompetitionRole  and r.name= :name")
    Optional<CompetitionRole> findByCompName(String name);

    void deleteByName(String name);

    @Query("select  r from Role r where  TYPE(r) = CompetitionRole ")
    List<CompetitionRole> getAllCompRoles();


    @Query("select  r from Role r where  TYPE(r) = CommunityRole ")
    List<CommunityRole> getAllCommunityRoles();


    @Query("select tr.role "
            + " from TipperRole tr"
            + " where tr.tipper.id=:tipperId")
    List<Role> findRolesByTipperId(Long tipperId);

}