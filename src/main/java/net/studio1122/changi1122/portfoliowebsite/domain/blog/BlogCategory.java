package net.studio1122.changi1122.portfoliowebsite.domain.blog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document("blog_categories")
public class BlogCategory {

    List<Category> category;

    public BlogCategory() {
        category = new ArrayList<Category>();
    }

}
