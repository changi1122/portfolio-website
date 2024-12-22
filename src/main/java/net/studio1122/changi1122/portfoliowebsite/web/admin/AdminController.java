package net.studio1122.changi1122.portfoliowebsite.web.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    /* View Rendering Method  */

    @GetMapping("/manage")
    public String adminHome(Model model) {

        model.addAttribute("content", "admin/fragment/home");

        return "admin/manage";
    }

}
