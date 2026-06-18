package sportbets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class FootballBetsApplication {

    private static final Logger log = LoggerFactory.getLogger(FootballBetsApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(FootballBetsApplication.class, args);
    }


    @Bean
    public CommandLineRunner run() {
        return runner -> {

            log.info("Start successful");
        };
    }

}

