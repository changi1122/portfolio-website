package net.studio1122.changi1122.portfoliowebsite.web.home;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogArticle;
import net.studio1122.changi1122.portfoliowebsite.domain.home.Home;
import net.studio1122.changi1122.portfoliowebsite.web.blog.BlogService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final BlogService blogService;

    /* CRUD Operation Methods */

    public String createHome(@Validated @RequestParam Home request) {
        homeService.createHome(request);
        return null;
    }

    /* View Rendering Method  */

    @GetMapping("/")
    public String index(@RequestParam(value = "ack", required = false) String accessKey, Model model) {

        if (accessKey == null) {
            accessKey = "default";
        }
        LocalDate today = LocalDate.now();

        Home home = homeService.readHome(accessKey, today);
        model.addAttribute("home", home);

        Pageable pageable = PageRequest.of(0, 4);
        List<BlogArticle> articles = blogService.list(pageable).getContent();
        model.addAttribute("articles", articles);

        return "index";
    }

    @GetMapping("/manage/home/list")
    public String homeList(Model model) {
        model.addAttribute("content", "admin/fragment/homeList");

        return "admin/manage";
    }

    @GetMapping("/manage/home/new")
    public String homeForm(Model model) {
        model.addAttribute("content", "admin/fragment/homeForm");

        return "admin/manage";
    }

}
