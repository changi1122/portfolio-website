package net.studio1122.changi1122.portfoliowebsite.domain.project;

import lombok.*;
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
    List<String> bodyLinks;

    boolean isHidden;

    @Builder
    public Project(String name, String description, String imageSrc, List<Keyword> keywords, List<String> links,
                   String category, List<String> badges, String body, List<String> bodyLinks) {
        this.name = name;
        this.description = description;
        this.imageSrc = imageSrc;
        this.keywords = keywords;
        this.links = links;
        this.category = category;
        this.badges = badges;
        this.body = body;
        this.bodyLinks = bodyLinks;

        isHidden = false;
    }
}
