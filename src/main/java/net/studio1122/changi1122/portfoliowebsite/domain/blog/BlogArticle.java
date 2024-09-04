package net.studio1122.changi1122.portfoliowebsite.domain.blog;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Document("blog_articles")
@Data
public class BlogArticle {

    @Id
    public String id;

    public String title;
    public String category;
    public String description;
    public LocalDateTime pubDate;
    public String link;

    @Field("image_src")
    public String imageSrc;

    public List<String> tags;

    public String getPubDateString() {
        return (pubDate != null) ? pubDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) : "";
    }
}
