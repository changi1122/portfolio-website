package net.studio1122.changi1122.portfoliowebsite.web.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.entity.BlogCategory;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BlogCategoryFormatterTest {

    DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        conversionService.addFormatter(new BlogCategoryFormatter());
    }

    @DisplayName("블로그 카테고리 설정을 위해 List<Category>를 파싱해 BlogCategory 객체를 만듭니다.")
    @Test
    void parse() {
        // given
        String categories = """
                        [ {
                          "text" : "프로그래밍",
                          "category" : "프로그래밍",
                          "subCategories" : [ ]
                        } ]""";

        // when
        BlogCategory category = conversionService.convert(categories, BlogCategory.class);

        // then
        assertThat(category.getCategory()).hasSize(1);
        assertThat(category.getCategory().get(0))
                .extracting("text", "category")
                .contains("프로그래밍", "프로그래밍");
    }

    @DisplayName("블로그 카테고리 설정 폼에 BlogCategory 객체를 List<Category> JSON 문자열로 변환합니다.")
    @Test
    void print() {
        // given
        List<Category> categories = List.of(
                new Category("프로그래밍", "프로그래밍")
        );
        BlogCategory category = new BlogCategory(categories);

        // when
        String result = conversionService.convert(category, String.class);

        // then
        assertThat(result.replaceAll("\\r\\n", "\n")).isEqualTo("""
                        [ {
                          "text" : "프로그래밍",
                          "category" : "프로그래밍",
                          "subCategories" : [ ]
                        } ]""");
    }
}