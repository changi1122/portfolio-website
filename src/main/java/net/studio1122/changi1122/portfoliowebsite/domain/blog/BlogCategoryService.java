package net.studio1122.changi1122.portfoliowebsite.domain.blog;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.entity.BlogCategory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogCategoryService {

    private final MongoTemplate mongoTemplate;

    public BlogCategory find() {
        BlogCategory category = mongoTemplate.findOne(new Query(), BlogCategory.class);
        if (category == null) {
            return new BlogCategory();
        }
        return category;
    }

    public void save(BlogCategory category) {
        mongoTemplate.remove(new Query(), BlogCategory.class);
        mongoTemplate.insert(category);
    }

}
