package ru.vels.taskplanner.api;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Secured("ROLE_USER")
    @GetMapping(path = "/user", produces = "application/json")
    public String user() {
        System.out.println("user");
        return "{\"role\":\"user\"}";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "/admin", produces = "application/json")
    public String admin() {
        System.out.println("admin");
        return "{\"role\":\"admin\"}";
    }
}
