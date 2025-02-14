package net.studio1122.changi1122.portfoliowebsite.web.common;

import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogArticle;
import net.studio1122.changi1122.portfoliowebsite.web.blog.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CommonController {

    public final BlogService blogService;

    @Autowired
    public CommonController(BlogService blogService) {
        this.blogService = blogService;
    }

    /* For test */
    @GetMapping("/project")
    public String project() {
        return "project";
    }

    /* For test */
    @GetMapping("/manage/project/list")
    public String projectList(Model model) {
        model.addAttribute("content", "admin/fragment/projectList");

        return "admin/manage";
    }

    /* For test */
    @GetMapping("/manage/project/new")
    public String projectEditor(Model model) {
        return "admin/projectEditor";
    }
}
