package sportbets.persistence.repository.competition;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sportbets.common.Country;
import sportbets.persistence.entity.competition.CompetitionFamily;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest()
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompetitionFamilyRepositoryTest {

    private CompetitionFamily testFamily;

    @Autowired
    private CompetitionFamilyRepository familyRepo;


    @Before
    public void setUp() {
        // Initialize test data before each test method
        testFamily = new CompetitionFamily("TestLiga", "2. Deutsche Fussball TestLiga", true, true, Country.GERMANY);

        familyRepo.save(testFamily);
    }

    @After
    public void tearDown() {
        // Release test data after each test method
        familyRepo.delete(testFamily);
    }

    @Test
    public void whenFindAll_thenCheckWithAllAnyMatchers() {
        // given
        Predicate<CompetitionFamily> p1 = g -> g.getName().equals("TestLiga");
        Predicate<CompetitionFamily> p2 = g -> g.getName().equals("EURO");

        // when
        List<CompetitionFamily> found = familyRepo.findAll();

        // then
        assertNotNull(found);
        assertTrue(found.stream().anyMatch(p1));
        assertTrue(found.stream().noneMatch(p2));
    }

    @Test
    public void givenCompFamily_whenSaved_thenCanBeFoundById() {

        CompetitionFamily family = familyRepo.findById(testFamily.getId()).orElse(null);
        assertNotNull(family);


        assertEquals(testFamily.getName(), family.getName());

    }

    @Test
    public void givenFamily_whenUpdated_thenCanBeFoundByIdWithUpdatedData() {
        testFamily.setName("UEFA European Championship");
        familyRepo.save(testFamily);

        CompetitionFamily updateFamily = familyRepo.findById(testFamily.getId()).orElse(null);

        assertNotNull(updateFamily);
        assertEquals("UEFA European Championship", updateFamily.getName());


    }

    @Test
    public void givenFamily_whenFindByNameCalled_thenFamilyIsFound() {
        CompetitionFamily foundFamily = familyRepo.findByName("TestLiga").orElse(null);

        assertNotNull(foundFamily);
        assertEquals(testFamily.getName(), foundFamily.getName());
    }
}
