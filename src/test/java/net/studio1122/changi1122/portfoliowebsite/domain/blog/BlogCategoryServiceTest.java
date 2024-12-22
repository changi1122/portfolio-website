package net.studio1122.changi1122.portfoliowebsite.domain.blog;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogCategoryServiceTest {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    BlogCategoryService blogCategoryService;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(BlogCategory.class);
    }

    @DisplayName("블로그 카테고리를 조회합니다.")
    @Test
    void find() {
        //given
        BlogCategory category = createCategory();
        mongoTemplate.insert(category);

        // when
        BlogCategory saved = blogCategoryService.find();

        // then
        assertThat(saved.getCategory()).hasSize(1)
                .extracting("text", "category")
                .containsExactlyInAnyOrder(
                        tuple("프로그래밍", "프로그래밍")
                );
    }

    @DisplayName("저장된 블로그 카테고리가 없으면 빈 배열을 가진 객체를 반환합니다.")
    @Test
    void findNoElement() {
        // when
        BlogCategory saved = blogCategoryService.find();

        // then
        assertThat(saved.getCategory()).isEmpty();
    }

    @DisplayName("블로그 카테고리를 저장합니다.")
    @Test
    void save() {
        //given
        BlogCategory category = createCategory();

        // when
        blogCategoryService.save(category);

        // then
        BlogCategory saved = blogCategoryService.find();
        assertThat(saved.getCategory()).hasSize(1)
                .extracting("text", "category")
                .containsExactlyInAnyOrder(
                        tuple("프로그래밍", "프로그래밍")
                );
    }

    private BlogCategory createCategory() {
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setCategory(List.of(
                new Category("프로그래밍", "프로그래밍",
                        List.of(new Category("C#", "프로그래밍%2FC%23"))
                )
        ));
        return blogCategory;
    }
}