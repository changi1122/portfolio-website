package net.studio1122.changi1122.portfoliowebsite.web.blog;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogArticle;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {

    public final BlogArticleRepository blogArticleRepository;


    public Page<BlogArticle> list(Pageable pageable) {
        return blogArticleRepository.findAllByOrderByPubDateDesc(pageable);
    }

    public Page<BlogArticle> listByCategory(Pageable pageable, String category) {
        return blogArticleRepository.findByCategoryOrderByPubDateDesc(pageable, category);
    }

    public Page<BlogArticle> listByQuery(Pageable pageable, String query) {
        return blogArticleRepository.findByTitleContainingOrderByPubDateDesc(pageable, query);
    }

    public Page<BlogArticle> listByTag(Pageable pageable, String tag) {
        return blogArticleRepository.findByTagsOrderByPubDateDesc(pageable, tag);
    }
}
