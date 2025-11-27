package sportbets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class TestProfileLiveTest {

    /**
     *  First create the testDB with name "bulitippertest" with application.properties
     * @return  data source.
     */
    @Bean
    @Profile("test")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/bulitipper2?createDatabaseIfNotExist=true");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSource.setUsername("root");
        dataSource.setPassword("admin");

        return dataSource;
    }

}
