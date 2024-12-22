package net.studio1122.changi1122.portfoliowebsite.web.resume;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.Resume;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    /* CRUD Operation Methods */



    /* View Rendering Method  */

    @GetMapping("/about")
    public String about(@RequestParam(value = "ack", required = false) String accessKey, Model model) {

        if (accessKey == null) {
            accessKey = "default";
        }
        LocalDate today = LocalDate.now();

        Resume resume = resumeService.readResume(accessKey, today);
        model.addAttribute("resume", resume);

        return "about";
    }

    @GetMapping("/manage/resume/list")
    public String resumeList(Model model) {
        model.addAttribute("content", "admin/fragment/resumeList");

        return "admin/manage";
    }

    @GetMapping("/manage/resume/new")
    public String resumeForm(Model model) {
        model.addAttribute("content", "admin/fragment/resumeForm");

        return "admin/manage";
    }

}
