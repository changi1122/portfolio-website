package net.studio1122.changi1122.portfoliowebsite.web.home;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogArticle;
import net.studio1122.changi1122.portfoliowebsite.domain.home.Home;
import net.studio1122.changi1122.portfoliowebsite.web.blog.BlogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final BlogService blogService;

    /* CRUD Operation Methods */

    @PostMapping("/home")
    public String createHome(@Validated @ModelAttribute("home") Home home, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (homeService.existsAccessKey(home.getAccessKey())) {
            bindingResult.rejectValue("accessKey", "home.accessKey",
                    "이미 존재하는 액세스 키입니다.");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.home",
                    bindingResult
            );
            redirectAttributes.addFlashAttribute("home", home);

            return "redirect:/manage/home/new";
        }

        homeService.createHome(home);

        return "redirect:/manage/home/list";
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
        List<BlogArticle> articles = blogService.listBlog(pageable).getContent();
        model.addAttribute("articles", articles);

        return "index";
    }

    @GetMapping("/manage/home/list")
    public String homeList(@RequestParam(defaultValue = "1") int page, Model model) {

        Page<Home> homes = homeService.listHome(PageRequest.of(page - 1, 8));

        addHomesToModel(model, homes, page);
        model.addAttribute("content", "admin/fragment/homeList");

        return "admin/manage";
    }

    @GetMapping("/manage/home/new")
    public String homeForm(Model model) {
        if (!model.containsAttribute("home")) {
            // default 객체가 존재하면 기본값으로 표시
            if (homeService.existsAccessKey("default")) {
                model.addAttribute("home",
                    homeService.readHome("default")
                );
            }
            else {
                model.addAttribute("home", new Home());
            }
        }

        model.addAttribute("action", "/home");
        model.addAttribute("content", "admin/fragment/homeForm");

        return "admin/manage";
    }

    @GetMapping("/manage/home/edit")
    public String homeEditForm(String accessKey, Model model) {

        // TODO id 존재하지 않는 경우 예외 처리
        Home home = homeService.readHome(accessKey);
        model.addAttribute("home", home);

        model.addAttribute("action", "/home/" + accessKey);
        model.addAttribute("content", "admin/fragment/homeForm");

        return "admin/manage";
    }

    private void addHomesToModel(Model model, Page<Home> homes, int page) {
        model.addAttribute("homes", homes);
        model.addAttribute("total", homes.getTotalPages());
        model.addAttribute("page", page);

        int start = page - (page - 1) % 5;
        model.addAttribute("start", start);
        model.addAttribute("end", Math.max(Math.min(start + 4, homes.getTotalPages()), 1));
    }
}
