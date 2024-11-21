package net.studio1122.changi1122.portfoliowebsite.domain.blog;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@Document("blog_articles")
public class BlogArticle {

    @Id
    private String id;

    private String title;
    private String category;
    private String description;
    private LocalDateTime pubDate;
    private String link;

    @Field("image_src")
    private String imageSrc;

    private List<String> tags;

    public String getPubDateString() {
        return (pubDate != null) ? pubDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) : "";
    }
}
