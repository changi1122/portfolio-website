package net.studio1122.changi1122.portfoliowebsite.web.home;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogService;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.entity.BlogArticle;
import net.studio1122.changi1122.portfoliowebsite.domain.home.HomeService;
import net.studio1122.changi1122.portfoliowebsite.domain.home.entity.Home;
import net.studio1122.changi1122.portfoliowebsite.global.metric.UserMetricRecorder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final BlogService blogService;
    private final UserMetricRecorder userMetricRecorder;

    /* CRUD Operation Methods */

    @PostMapping("/home")
    public String createHome(@Validated @ModelAttribute("home") Home home, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (homeService.existsAccessKey(home.getAccessKey())) {
            bindingResult.rejectValue("accessKey", "home.accessKey",
                    "이미 존재하는 액세스 키입니다.");
        }

        if (home.getProjects() == null || home.getProjects().size() != 3) {
            bindingResult.reject("home.projectSizeNotAllowed",
                    "홈화면의 프로젝트의 수는 3개여야 합니다.");
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

    @PostMapping("/home/{accessKey}")
    public String updateHome(@PathVariable("accessKey") String accessKey, @Validated @ModelAttribute("home") Home home,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (home.getProjects() == null || home.getProjects().size() != 3) {
            bindingResult.reject("home.projectSizeNotAllowed",
                    "홈화면의 프로젝트의 수는 3개여야 합니다.");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.home",
                    bindingResult
            );
            redirectAttributes.addFlashAttribute("home", home);

            return "redirect:/manage/home/edit?accessKey=" + accessKey;
        }

        homeService.updateHome(accessKey, home);

        return "redirect:/manage/home/list";
    }

    @DeleteMapping("/home/{accessKey}")
    public ResponseEntity<Void> deleteHome(@PathVariable String accessKey) {
        homeService.deleteHome(accessKey);

        return ResponseEntity.ok().build();
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

        userMetricRecorder.countPageView("/", accessKey);
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
