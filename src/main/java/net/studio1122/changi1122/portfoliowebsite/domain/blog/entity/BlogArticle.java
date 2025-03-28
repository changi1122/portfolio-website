package net.studio1122.changi1122.portfoliowebsite.domain.blog.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document("blog_articles")
public class BlogArticle {

    @Id
    private String id;

    @NotBlank
    private String title;
    @NotBlank
    private String category;

    private String description;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime pubDate;
    @NotBlank
    private String link;

    @Field("image_src")
    private String imageSrc;

    private List<String> tags;

    public String getPubDateString() {
        return (pubDate != null) ? pubDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) : "";
    }

    @Builder
    public BlogArticle(String title, String category, String description, LocalDateTime pubDate, String link,
                       String imageSrc, List<String> tags) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
        this.imageSrc = imageSrc;
        this.tags = tags;
    }
}
