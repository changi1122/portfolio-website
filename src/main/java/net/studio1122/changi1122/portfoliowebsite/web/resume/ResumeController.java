package net.studio1122.changi1122.portfoliowebsite.web.resume;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    /* CRUD Operation Methods */

    @PostMapping("/resume")
    public String createResume(@Validated @ModelAttribute("resume") Resume resume, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if (resumeService.existsAccessKey(resume.getAccessKey())) {
            bindingResult.rejectValue("accessKey", "resume.accessKey",
                    "이미 존재하는 액세스 키입니다.");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.resume",
                    bindingResult
            );
            redirectAttributes.addFlashAttribute("resume", resume);

            return "redirect:/manage/resume/new";
        }

        resumeService.createResume(resume);

        return "redirect:/manage/resume/list";
    }

    @PostMapping("/resume/{accessKey}")
    public String updateResume(@PathVariable String accessKey, @Validated @ModelAttribute("resume") Resume resume,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.resume",
                    bindingResult
            );
            redirectAttributes.addFlashAttribute("resume", resume);

            return "redirect:/manage/resume/edit?accessKey=" + accessKey;
        }

        // TODO id 존재하지 않는 경우 예외 처리
        resumeService.updateResume(accessKey, resume);

        return "redirect:/manage/resume/list";
    }

    @DeleteMapping("/resume/{accessKey}")
    public String deleteResume(@PathVariable String accessKey) {
        // TODO id 존재하지 않는 경우 예외 처리
        resumeService.deleteResume(accessKey);

        return "redirect:/manage/resume/list";
    }


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
    public String resumeList(@RequestParam(defaultValue = "1") int page, Model model) {

        Page<Resume> resumes = resumeService.listResume(PageRequest.of(page - 1, 8));

        addResumesToModel(model, resumes, page);
        model.addAttribute("content", "admin/fragment/resumeList");

        return "admin/manage";
    }

    @GetMapping("/manage/resume/new")
    public String resumeForm(Model model) {
        if (!model.containsAttribute("resume")) {
            // default 객체가 존재하면 기본값으로 표시
            if (resumeService.existsAccessKey("default")) {
                model.addAttribute("resume",
                        resumeService.readResume("default")
                );
            }
            else {
                model.addAttribute("resume", new Resume());
            }
        }

        model.addAttribute("action", "/resume");
        model.addAttribute("content", "admin/fragment/resumeForm");

        return "admin/manage";
    }

    @GetMapping("/manage/resume/edit")
    public String resumeEditForm(String accessKey, Model model) {

        // TODO id 존재하지 않는 경우 예외 처리
        Resume resume = resumeService.readResume(accessKey);
        model.addAttribute("resume", resume);

        model.addAttribute("action", "/resume/" + accessKey);
        model.addAttribute("content", "admin/fragment/resumeForm");

        return "admin/manage";
    }

    private void addResumesToModel(Model model, Page<Resume> resumes, int page) {
        model.addAttribute("resumes", resumes);
        model.addAttribute("total", resumes.getTotalPages());
        model.addAttribute("page", page);

        int start = page - (page - 1) % 5;
        model.addAttribute("start", start);
        model.addAttribute("end", Math.max(Math.min(start + 4, resumes.getTotalPages()), 1));
    }
}
