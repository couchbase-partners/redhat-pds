package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWildFlyController {

    @RequestMapping("/hello")
    public String sayHello(){
        return ("Greetings from Spring Boot!");
    }

}