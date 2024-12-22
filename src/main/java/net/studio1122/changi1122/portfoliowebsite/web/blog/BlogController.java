package net.studio1122.changi1122.portfoliowebsite.web.blog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogArticle;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogArticleRepository blogArticleRepository;
    public final BlogService blogService;

    /* CRUD Operation Methods */

    @PostMapping("/blog")
    public String createBlogArticle(@Validated @ModelAttribute("article") BlogArticle article, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.article",
                    bindingResult
            );
            redirectAttributes.addFlashAttribute("article", article);

            return "redirect:/manage/blog/new";
        }

        blogService.createBlogArticle(article);

        return "redirect:/manage/blog/list";
    }

    /* View Rendering Method  */

    @GetMapping("/blog/{page}")
    public String blog(@PathVariable int page,
                       @RequestParam(defaultValue = "") String category,
                       @RequestParam(defaultValue = "") String query,
                       @RequestParam(defaultValue = "") String tag, Model model) {

        Pageable pageable = PageRequest.of(page - 1, 8);
        Page<BlogArticle> articles;

        if (!tag.isBlank())
            articles = blogService.listByTag(pageable, tag);
        else if (!query.isBlank())
            articles = blogService.listByQuery(pageable, query);
        else if (!category.isBlank())
            articles = blogService.listByCategory(pageable, category);
        else
            articles = blogService.list(pageable);

        model.addAttribute("articles", articles);
        model.addAttribute("total", articles.getTotalPages());
        model.addAttribute("page", page);

        int start = page - (page - 1) % 5;
        model.addAttribute("start", start);
        model.addAttribute("end", Math.max(Math.min(start + 4, articles.getTotalPages()), 1));
        model.addAttribute("categoryString", category.equals("") ? "전체 카테고리" : category);

        return "blog";
    }

    @GetMapping("/manage/blog/list")
    public String blogList(Model model) {
        model.addAttribute("content", "admin/fragment/blogList");

        return "admin/manage";
    }

    @GetMapping("/manage/blog/new")
    public String blogForm(Model model) {
        if (!model.containsAttribute("article"))
            model.addAttribute("article", new BlogArticle());

        model.addAttribute("content", "admin/fragment/blogForm");

        return "admin/manage";
    }

    @GetMapping("/manage/blog/category")
    public String blogCategoryForm(Model model) {
        model.addAttribute("content", "admin/fragment/blogCategoryForm");

        return "admin/manage";
    }

}
