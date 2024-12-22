package net.studio1122.changi1122.portfoliowebsite.domain.blog;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    String text;
    String category;
    List<Category> subCategories = new ArrayList<>();

    public Category(String text, String category) {
        this.text = text;
        this.category = category;
    }

    public Category(String text, String category, List<Category> subCategories) {
        this.text = text;
        this.category = category;
        this.subCategories = subCategories;
    }
}
