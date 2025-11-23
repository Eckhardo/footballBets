package sportbets.persistence.dao;

import sportbets.persistence.entity.CompetitionFamily;

import java.util.List;

public interface CompetitionFamilyDAO {

    void save(CompetitionFamily theComp);

    CompetitionFamily findById(Long id);

    List<CompetitionFamily> findAll();

    List<CompetitionFamily> findByName(String theName);

    void update(CompetitionFamily theComp);

    void delete(Long id);
    void deleteByName(String name );

    int deleteAll();
}
