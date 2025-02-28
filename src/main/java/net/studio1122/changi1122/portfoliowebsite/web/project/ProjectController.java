package net.studio1122.changi1122.portfoliowebsite.web.project;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.project.Keyword;
import net.studio1122.changi1122.portfoliowebsite.domain.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /* CRUD Operation Methods */
    @PostMapping("/project")
    public String createProject(@ModelAttribute("project") Project project) {

        projectService.createProject(project);
        return "redirect:/manage/project/list";
    }

    @PostMapping("/project/{id}")
    public String updateProject(@PathVariable  String id, @ModelAttribute("project") Project project) {

        projectService.updateProject(id, project);
        return "redirect:/manage/project/list";
    }

    @PutMapping("/project/{id}/hidden")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void toggleProjectVisibility(@PathVariable String id) {
        projectService.toggleProjectVisibility(id);
    }

    @DeleteMapping("/project/{id}")
    public String deleteProject(@PathVariable  String id) {

        projectService.deleteProject(id);
        return "redirect:/manage/project/list";
    }


    /* View Rendering Method  */

    @GetMapping("/project/{id}")
    public String readProject(@PathVariable String id, Model model) {

        Project project = projectService.readProject(id);
        project.setBodyHtml(URLDecoder.decode(project.getBodyHtml(), StandardCharsets.UTF_8));

        model.addAttribute("project", project);
        return "projectDetail";
    }

    @GetMapping("/manage/project/list")
    public String projectList(@RequestParam(defaultValue = "1") int page, Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1, 8, Sort.by(Sort.Direction.ASC, "order"));
        Page<Project> projects = projectService.listProject(pageRequest);

        addProjectsToModel(model, projects, page);
        model.addAttribute("content", "admin/fragment/projectList");

        return "admin/manage";
    }

    @GetMapping("/manage/project/new")
    public String projectForm(Model model) {

        Project project = createProject();
        model.addAttribute("project", project);
        model.addAttribute("action", "/project");

        return "admin/projectEditor";
    }

    @GetMapping("/manage/project/edit")
    public String projectEditForm(String id, Model model) {

        Project project = projectService.readProject(id);
        project.setBody(URLDecoder.decode(project.getBody(), StandardCharsets.UTF_8));
        project.setBodyHtml(URLDecoder.decode(project.getBodyHtml(), StandardCharsets.UTF_8));

        model.addAttribute("project", project);
        model.addAttribute("action", "/project/" + id);

        return "admin/projectEditor";
    }

    private void addProjectsToModel(Model model, Page<Project> projects, int page) {
        model.addAttribute("projects", projects);
        model.addAttribute("total", projects.getTotalPages());
        model.addAttribute("page", page);

        int start = page - (page - 1) % 5;
        model.addAttribute("start", start);
        model.addAttribute("end", Math.max(Math.min(start + 4, projects.getTotalPages()), 1));
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
