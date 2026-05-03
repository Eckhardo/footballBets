package sportbets;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sportbets.persistence.repository.authorization.RoleRepository;
import sportbets.persistence.repository.authorization.TipperRoleRepository;
import sportbets.persistence.repository.community.CommunityMembershipRepository;
import sportbets.persistence.repository.community.CommunityRepository;
import sportbets.persistence.repository.community.TipperRepository;
import sportbets.persistence.repository.competition.*;
import sportbets.persistence.repository.tipps.TippConfigRepository;

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

