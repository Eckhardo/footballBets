package sportbets.persistence.dao;

import sportbets.persistence.entity.Competition;

import java.util.List;

public interface CompetitionDAO {

    void save(Competition theComp);

    Competition findById(Long id);

    List<Competition> findAll();

    List<Competition> findByName(String theName);

    void update(Competition theComp);
    void delete(Long id);

    int deleteAll();
}
