package net.studio1122.changi1122.portfoliowebsite.domain.blog;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/blog")
public class BlogController {

    public final BlogService blogService;


    @GetMapping("/{page}")
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

}
