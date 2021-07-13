package hello.itemservice.upload;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasicController {

    @GetMapping("/upload")
    public String home() {
        return "upload/home";
    }
}
