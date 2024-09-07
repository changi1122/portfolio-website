package net.studio1122.changi1122.portfoliowebsite.domain.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    public final BlogArticleRepository blogArticleRepository;

    @Autowired
    public BlogService(final BlogArticleRepository blogArticleRepository) {
        this.blogArticleRepository = blogArticleRepository;
    }

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
