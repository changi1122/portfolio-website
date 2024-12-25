package net.studio1122.changi1122.portfoliowebsite.web.blog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogArticle;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogCategory;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogCategoryService;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BlogController {

    public final BlogService blogService;
    public final BlogCategoryService blogCategoryService;
    public final ObjectMapper objectMapper;

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

    @PostMapping("/blog/{id}")
    public String updateBlogArticle(@PathVariable String id, @Validated @ModelAttribute("article") BlogArticle article,
                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.article",
                    bindingResult
            );
            redirectAttributes.addFlashAttribute("article", article);

            return "redirect:/manage/blog/edit?id=" + id;
        }

        // TODO id 존재하지 않는 경우 예외 처리
        blogService.updateBlogArticle(id, article);

        return "redirect:/manage/blog/list";
    }

    @DeleteMapping("/blog/{id}")
    public String deleteBlogArticle(@PathVariable String id) {
        // TODO id 존재하지 않는 경우 예외 처리
        blogService.deleteBlogArticle(id);

        return "redirect:/manage/blog/list";
    }

    @PostMapping("/blog/category")
    public String updateBlogCategory(@ModelAttribute("category") String categoryString) throws JsonProcessingException {
        // TODO 파싱 예외 처리
        BlogCategory category = objectMapper.readValue(categoryString, BlogCategory.class);
        blogCategoryService.save(category);

        return "redirect:/manage/blog/category";
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
            articles = blogService.listBlogByTag(pageable, tag);
        else if (!query.isBlank())
            articles = blogService.listBlogByQuery(pageable, query);
        else if (!category.isBlank())
            articles = blogService.listBlogByCategory(pageable, category);
        else
            articles = blogService.listBlog(pageable);

        addArticlesToModel(model, articles, page);

        model.addAttribute("categories", blogCategoryService.find());
        model.addAttribute("categoryString", category.equals("") ? "전체 카테고리" : category);

        return "blog";
    }

    @GetMapping("/manage/blog/list")
    public String blogList(@RequestParam(defaultValue = "1") int page, Model model) {

        Page<BlogArticle> articles = blogService.listBlog(PageRequest.of(page - 1, 8));

        addArticlesToModel(model, articles, page);
        model.addAttribute("content", "admin/fragment/blogList");

        return "admin/manage";
    }

    @GetMapping("/manage/blog/new")
    public String blogForm(Model model) {
        if (!model.containsAttribute("article"))
            model.addAttribute("article", new BlogArticle());

        model.addAttribute("action", "/blog");
        model.addAttribute("content", "admin/fragment/blogForm");

        return "admin/manage";
    }

    @GetMapping("/manage/blog/edit")
    public String blogEditForm(String id, Model model) {

        // TODO id 존재하지 않는 경우 예외 처리
        BlogArticle article = blogService.readBlogArticle(id);
        model.addAttribute("article", article);

        model.addAttribute("action", "/blog/" + id);
        model.addAttribute("content", "admin/fragment/blogForm");

        return "admin/manage";
    }

    @GetMapping("/manage/blog/category")
    public String blogCategoryForm(Model model) throws JsonProcessingException {
        BlogCategory category = blogCategoryService.find();
        if (category.getCategory().isEmpty()) {
            category.getCategory().add(new Category("카테고리", "category",
                    List.of(new Category("서브 카테고리", "category%2Fsub"))
            ));
        }

        model.addAttribute(
                "category",
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(category)
        );

        model.addAttribute("content", "admin/fragment/blogCategoryForm");
        return "admin/manage";
    }

    private void addArticlesToModel(Model model, Page<BlogArticle> articles, int page) {
        model.addAttribute("articles", articles);
        model.addAttribute("total", articles.getTotalPages());
        model.addAttribute("page", page);

        int start = page - (page - 1) % 5;
        model.addAttribute("start", start);
        model.addAttribute("end", Math.max(Math.min(start + 4, articles.getTotalPages()), 1));
    }
}
