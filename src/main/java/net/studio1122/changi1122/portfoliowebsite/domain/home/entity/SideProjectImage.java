package net.studio1122.changi1122.portfoliowebsite.domain.home.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.entity.record.Keyword;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SideProjectImage {

    String name;
    String description;
    String imageSrc;
    String link;
    List<Keyword> keywords;
    String themeColor;

    @Builder
    public SideProjectImage(String name, String description, String imageSrc, String link,
                            List<Keyword> keywords, String themeColor) {
        this.name = name;
        this.description = description;
        this.imageSrc = imageSrc;
        this.link = link;
        this.keywords = keywords;
        this.themeColor = themeColor;
    }
}
