package net.studio1122.changi1122.portfoliowebsite.web.blog;

import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogArticle;
import net.studio1122.changi1122.portfoliowebsite.domain.blog.BlogArticleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogServiceTest {

    @Autowired
    BlogArticleRepository blogArticleRepository;
    @Autowired
    BlogService blogService;

    @AfterEach
    void tearDown() {
        blogArticleRepository.deleteAll();
    }

    @DisplayName("새로운 블로그 게시글을 추가합니다.")
    @Test
    void createBlogArticle() {
        // given
        BlogArticle request = createBlogArticle("제목", "카테고리");

        // when
        request = blogService.createBlogArticle(request);

        // then
        BlogArticle saved = blogArticleRepository.findById(request.getId()).orElseThrow();

        assertThat(saved)
                .extracting("title", "category", "description", "link", "imageSrc")
                .containsExactly(
                        "제목", "카테고리", "설명", "https://github.com", "https://picsum.photos/300/200"
                );
        assertThat(saved.getPubDate()).isEqualTo(request.getPubDate());
        assertThat(saved.getTags()).hasSize(2)
                .containsExactlyInAnyOrder("태그1", "태그2");
    }

    @DisplayName("블로그 게시글을 수정합니다.")
    @Test
    void updateBlogArticle() {
        //given
        BlogArticle article = createBlogArticle("제목", "카테고리");
        article = blogService.createBlogArticle(article);

        BlogArticle request = BlogArticle.builder()
                .title("제목1")
                .category("카테고리1")
                .description("설명1")
                .pubDate(LocalDateTime.of(
                        LocalDate.of(2025, 1, 2), LocalTime.of(9, 0)
                ))
                .link("link1")
                .imageSrc("src1")
                .tags(List.of("태그1"))
                .build();

        // when
        blogService.updateBlogArticle(article.getId(), request);

        // then
        BlogArticle saved = blogArticleRepository.findById(article.getId()).orElseThrow();

        assertThat(saved)
                .extracting("title", "category", "description", "link", "imageSrc")
                .containsExactly(
                        "제목1", "카테고리1", "설명1", "link1", "src1"
                );
        assertThat(saved.getPubDate()).isEqualTo(request.getPubDate());
        assertThat(saved.getTags()).hasSize(1)
                .containsExactlyInAnyOrder("태그1");
    }

    @DisplayName("존재하지 않는 id로 블로그 게시글을 수정하면 오류가 반환됩니다.")
    @Test
    void updateBlogArticleNoSuchId() {
        //given
        BlogArticle request = createBlogArticle("제목1", "카테고리1");
    
        // when then
        assertThatThrownBy(() -> blogService.updateBlogArticle("no-such-id", request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("블로그 게시글을 삭제합니다.")
    @Test
    void deleteBlogArticle() {
        //given
        BlogArticle article = createBlogArticle("제목", "카테고리");
        article = blogService.createBlogArticle(article);

        // when
        blogService.deleteBlogArticle(article.getId());

        // then
        assertThat(blogArticleRepository.findById(article.getId()).isEmpty()).isTrue();
    }

    @DisplayName("존재하지 않는 id로 블로그 게시글을 삭제하면 오류가 반환됩니다.")
    @Test
    void deleteBlogArticleNoSuchId() {
        // when then
        assertThatThrownBy(() -> blogService.deleteBlogArticle("no-such-id"))
                .isInstanceOf(NoSuchElementException.class);
    }

    public BlogArticle createBlogArticle(String title, String category) {
        return BlogArticle.builder()
                .title(title)
                .category(category)
                .description("설명")
                .pubDate(LocalDateTime.of(
                        LocalDate.of(2025, 1, 1), LocalTime.of(10, 0))
                )
                .link("https://github.com")
                .imageSrc("https://picsum.photos/300/200")
                .tags(List.of("태그1", "태그2"))
                .build();
    }
}