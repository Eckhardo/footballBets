package sportbets.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class IntroContoller {


    @Value("${spring.application.name}")
    private String appName;

    @GetMapping(value = "/")
    public String welcome() {
        return "Hello Spring Bets! with name=" + appName;
    }
}
