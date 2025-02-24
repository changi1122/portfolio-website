package net.studio1122.changi1122.portfoliowebsite.web.common;

import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogArticle;
import net.studio1122.changi1122.portfoliowebsite.domain.project.Keyword;
import net.studio1122.changi1122.portfoliowebsite.domain.project.Project;
import net.studio1122.changi1122.portfoliowebsite.web.blog.BlogService;
import net.studio1122.changi1122.portfoliowebsite.web.project.MarkdownConst;
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

        Project project = createProject();
        model.addAttribute("project", project);

        return "admin/projectEditor";
    }


    private Project createProject() {
        return Project.builder()
                .name("프로젝트 이름 테스트")
                .description("주제")
                .imageSrc("/")
                .keywords(List.of(
                        new Keyword("기술", "Spring")
                ))
                .links(List.of("<a>소개</a>"))
                .category("이름 위 표시 문자열")
                .badges(List.of("Spring", "React"))
                .body(MarkdownConst.MARKDOWN_BODY_DEFAULT)
                .bodyLinks(List.of("<a>소개ewfwef</a>"))
                .themeColor("#999999")
                .build();
    }
}
