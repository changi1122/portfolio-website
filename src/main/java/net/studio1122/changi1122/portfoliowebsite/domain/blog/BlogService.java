package net.studio1122.changi1122.portfoliowebsite.domain.blog;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.entity.BlogArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {

    private final BlogArticleRepository blogArticleRepository;

    @Transactional
    public BlogArticle createBlogArticle(BlogArticle request) {
        return blogArticleRepository.save(request);
    }

    @Transactional
    public BlogArticle updateBlogArticle(String id, BlogArticle request) {
        BlogArticle article = blogArticleRepository.findById(id).orElseThrow();

        article.setTitle(request.getTitle());
        article.setCategory(request.getCategory());
        article.setDescription(request.getDescription());
        article.setPubDate(request.getPubDate());
        article.setLink(request.getLink());
        article.setImageSrc(request.getImageSrc());
        article.setTags(request.getTags());

        return blogArticleRepository.save(article);
    }

    @Transactional
    public void deleteBlogArticle(String id) {
        BlogArticle article = blogArticleRepository.findById(id).orElseThrow();
        blogArticleRepository.delete(article);
    }

    public BlogArticle readBlogArticle(String id) {
        return blogArticleRepository.findById(id).orElseThrow();
    }

    public Page<BlogArticle> listBlog(Pageable pageable) {
        return blogArticleRepository.findAllByOrderByPubDateDesc(pageable);
    }

    public Page<BlogArticle> listBlogByCategory(Pageable pageable, String category) {
        return blogArticleRepository.findByCategoryContainingOrderByPubDateDesc(pageable, category);
    }

    public Page<BlogArticle> listBlogByQuery(Pageable pageable, String query) {
        return blogArticleRepository.findByTitleContainingOrderByPubDateDesc(pageable, query);
    }

    public Page<BlogArticle> listBlogByTag(Pageable pageable, String tag) {
        return blogArticleRepository.findByTagsOrderByPubDateDesc(pageable, tag);
    }
}
