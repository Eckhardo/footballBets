package sportbets.persistence.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.dao.CompetitionDAO;
import sportbets.persistence.entity.competition.Competition;

import java.util.List;


/**
 * An alternative to DAOs  specified by  {{@link org.springframework.data.jpa.repository.JpaRepository}}
 * Here we use real DAOs that use the Hibernate Entity Manager directly
 */
@Repository
public class CompetitionDAOImpl implements CompetitionDAO {

    // define field for entity manager
    @PersistenceContext
    private final EntityManager entityManager;

    // inject entity manager using constructor injection
    @Autowired
    public CompetitionDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // implement save method
    @Override
    @Transactional
    public void save(Competition theComp) {
        entityManager.persist(theComp);
    }

    @Override
    public Competition findById(Long id) {
        return entityManager.find(Competition.class, id);
    }

    @Override
    public List<Competition> findAll() {
        // create query
        TypedQuery<Competition> theQuery = entityManager.createQuery("FROM Competition", Competition.class);

        // return query results
        return theQuery.getResultList();
    }

    @Override
    public List<Competition> findByName(String theName) {
        // create query
        TypedQuery<Competition> theQuery = entityManager.createQuery(
                "FROM Competition WHERE name=:theData", Competition.class);

        // set query parameters
        theQuery.setParameter("theData", theName);

        // return query results
        return theQuery.getResultList();
    }

    @Override
    @Transactional
    public void update(Competition theCompetition) {
        entityManager.merge(theCompetition);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        // retrieve the student
        Competition theCompetition = entityManager.find(Competition.class, id);

        // delete the student
        entityManager.remove(theCompetition);
    }


    @Override
    @Transactional
    public int deleteAll() {

        return entityManager.createQuery("DELETE FROM Competition").executeUpdate();
    }
}
