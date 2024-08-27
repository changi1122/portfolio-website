package net.studio1122.changi1122.portfoliowebsite.domain.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CommonController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    /* For test */
    @GetMapping("/blog/{page}")
    public String blog(@PathVariable Integer page) {
        return "blog";
    }
}
