package net.studio1122.changi1122.portfoliowebsite.domain.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogArticleRepository extends MongoRepository<BlogArticle, String> {

    Page<BlogArticle> findAllByOrderByPubDateDesc(Pageable pageable);

    Page<BlogArticle> findByCategoryContainingOrderByPubDateDesc(Pageable pageable, String category);

    Page<BlogArticle> findByTitleContainingOrderByPubDateDesc(Pageable pageable, String title);

    Page<BlogArticle> findByTagsOrderByPubDateDesc(Pageable pageable, String tag);
}
