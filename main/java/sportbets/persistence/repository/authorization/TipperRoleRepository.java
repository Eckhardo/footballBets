package sportbets.persistence.repository.authorization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.authorization.TipperRole;

import java.util.List;

@Repository
public interface TipperRoleRepository extends JpaRepository<TipperRole, Long> {

    @Query("select  tr from TipperRole tr join fetch  tr.tipper t"
            + " where  t.id=:tipperId")
    List<TipperRole> getAllForTipper(Long tipperId);

}