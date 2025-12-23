package sportbets.persistence.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.authorization.Role;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.Competition;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role>findByName (String name);
    void  deleteByName(String name);

    @Query("select  r from Role r where  TYPE(r) = CompetitionRole ")
    List<Role> getAllCompRolesForTipper(Long tipperId);


}