package es.omarall.restical;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @RequestMapping("/")
    public String greeting(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "time", required = true) String time,
            @RequestParam(value = "duration", required = false) String duration) {
        return "greeting";
    }

}