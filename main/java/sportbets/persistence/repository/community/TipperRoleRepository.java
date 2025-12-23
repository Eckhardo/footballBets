package sportbets.persistence.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.authorization.TipperRole;
import sportbets.persistence.entity.community.Tipper;
import sportbets.persistence.entity.competition.CompetitionTeam;

import java.util.List;
import java.util.Optional;
@Repository
public interface TipperRoleRepository extends JpaRepository<TipperRole, Long> {

    @Query("select  tr from TipperRole tr join fetch  tr.tipper t"
            + " where  t.id=:tipperId")
    List<TipperRole> getAllForTipper(Long tipperId);

}