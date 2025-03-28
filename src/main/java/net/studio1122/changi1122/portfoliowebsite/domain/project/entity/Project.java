package net.studio1122.changi1122.portfoliowebsite.domain.project.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document("project")
public class Project {

    @Id
    private String id;

    String name;
    String description;
    String imageSrc;
    List<Keyword> keywords;
    List<String> links;

    String category;
    List<String> badges;
    String body;
    String bodyHtml;
    List<String> bodyLinks;

    String themeColor;
    int order;
    boolean isHidden;

    @Builder
    public Project(String name, String description, String imageSrc, List<Keyword> keywords, List<String> links,
                   String category, List<String> badges, String body, String bodyHtml, List<String> bodyLinks,
                   Integer order, String themeColor) {
        this.name = name;
        this.description = description;
        this.imageSrc = imageSrc;
        this.keywords = keywords;
        this.links = links;
        this.category = category;
        this.badges = badges;
        this.body = body;
        this.bodyHtml = bodyHtml;
        this.bodyLinks = bodyLinks;
        this.order = order == null ? 0 : order;
        this.themeColor = themeColor;

        isHidden = false;
    }
}
