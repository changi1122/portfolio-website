package net.studio1122.changi1122.portfoliowebsite.domain.home;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HomeRepositoryTest {

    @Autowired
    HomeRepository homeRepository;

    @AfterEach
    void tearDown() {
        homeRepository.deleteAll();
    }

    @Test
    @DisplayName("홈 화면객체를 AccessKey로 조회합니다.")
    void findByAccessKey() {
        // given
        LocalDate today = LocalDate.of(2024, 12, 31);
        Home home = createHome("default", LocalDate.of(2024, 12, 31));

        // when
        homeRepository.save(home);
        Home saved = homeRepository.findByAccessKeyAndExpireDateGreaterThanEqual("default", today)
                .orElseThrow();

        // then
        assertThat(saved.getIntro()).isEqualTo(home.getIntro());
        assertThat(saved.getInterests()).hasSize(2)
                .containsExactlyInAnyOrder("Spring", "추천시스템");
        assertThat(saved.getQuestions()).hasSize(2)
                .extracting("question", "answer")
                .containsExactlyInAnyOrder(
                        tuple("질문?", "대답"),
                        tuple("질문2?", "대답")
                );
        assertThat(saved.getExpireDate()).isEqualTo(home.getExpireDate());
    }

    @Test
    @DisplayName("만료된 홈화면 객체를 조회하면 빈 Optional 객체가 반환됩니다.")
    void findByAccessKeyExpired() {
        // given
        LocalDate today = LocalDate.of(2025, 1, 1);
        Home home = createHome("access-key", LocalDate.of(2024, 12, 31));

        // when
        homeRepository.save(home);
        Optional<Home> saved = homeRepository.findByAccessKeyAndExpireDateGreaterThanEqual(
                "access-key",
                today
        );

        // then
        assertThat(saved.isEmpty()).isTrue();
    }

    private Home createHome(String accessKey, LocalDate expireDate) {
        return Home.builder()
                .accessKey(accessKey)
                .interests(List.of("Spring", "추천시스템"))
                .intro("안녕하세요!")
                .questions(List.of(
                        new Question(1, "질문?", "대답"),
                        new Question(2, "질문2?", "대답")
                ))
                .expireDate(expireDate)
                .build();
    }
}