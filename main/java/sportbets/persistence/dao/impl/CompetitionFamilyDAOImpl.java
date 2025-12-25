package sportbets.persistence.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.dao.CompetitionFamilyDAO;
import sportbets.persistence.entity.competition.CompetitionFamily;

import java.util.List;


/**
 * An alternative to DAOs  specified by  {{@link org.springframework.data.jpa.repository.JpaRepository}}
 * Here we use real DAOs that use the Hibernate Entity Manager directly
 */
@Repository
public class CompetitionFamilyDAOImpl implements CompetitionFamilyDAO {

    // define field for entity manager
    @PersistenceContext
    private final EntityManager entityManager;

    // inject entity manager using constructor injection
    @Autowired
    public CompetitionFamilyDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // implement save method
    @Override
    @Transactional
    public void save(CompetitionFamily theComp) {
        entityManager.persist(theComp);
    }

    @Override
    public CompetitionFamily findById(Long id) {
        return entityManager.find(CompetitionFamily.class, id);
    }

    @Override
    public List<CompetitionFamily> findAll() {
        // create query
        TypedQuery<CompetitionFamily> theQuery = entityManager.createQuery("FROM CompetitionFamily", CompetitionFamily.class);

        // return query results
        return theQuery.getResultList();
    }

    @Override
    public List<CompetitionFamily> findByName(String theName) {
        // create query
        TypedQuery<CompetitionFamily> theQuery = entityManager.createQuery(
                "FROM CompetitionFamily WHERE name=:theData", CompetitionFamily.class);

        // set query parameters
        theQuery.setParameter("theData", theName);

        // return query results
        return theQuery.getResultList();
    }

    @Override
    @Transactional
    public void update(CompetitionFamily theCompetitionFamily) {
        entityManager.merge(theCompetitionFamily);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        // retrieve the student
        CompetitionFamily theCompetitionFamily = entityManager.find(CompetitionFamily.class, id);

        // delete the student
        entityManager.remove(theCompetitionFamily);
    }

    @Override
    @Transactional
    public void deleteByName(String theName) {
        // set query parameters
        entityManager.createQuery("delete CompetitionFamily cf where cf.name = :name")
                .setParameter("name", theName).executeUpdate();

    }

    @Override
    @Transactional
    public int deleteAll() {

        int numRowsDeleted = entityManager.createQuery("DELETE FROM CompetitionFamily").executeUpdate();

        return numRowsDeleted;
    }
}
