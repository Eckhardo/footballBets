package sportbets.persistence.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sportbets.persistence.entity.community.Tipper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TipperRepository extends JpaRepository<Tipper, Long> {
    Optional<Tipper> findByUsername(String username);

    void deleteByUsername(String username);


    @Query(" select t from Tipper t" +
            " where t.username =:username and t.passwort= :password  ")
    Optional<Tipper> authenticateTipper(String username, String password);

    @Query(" select t from Tipper t" +
            " where t.username =:username ")
    Optional<Tipper> checkUserName(String username);

    boolean existsByUsername(String userName);

    @Query("SELECT t FROM Tipper t WHERE t.id IN :ids")
    List<Tipper> findBySpecificIds(@Param("ids") Collection<Long> ids);
}