package ua.ldv.server.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@RequestMapping("/test/")
public class TestController {

    @Secured("ROLE_ADMIN")
    @GetMapping("admin")
    public String testAdmin(){
        return "test ADMIN controller";
    }

}
