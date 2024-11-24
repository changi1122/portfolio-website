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
    @DisplayName("홈 화면 객체를 AccessKey로 조회합니다.")
    void findByAccessKey() {
        // given
        LocalDate today = LocalDate.of(2024, 12, 31);

        Home home = createHome("default", LocalDate.of(2024, 12, 31));

        // when
        homeRepository.save(home);
        Home found = homeRepository.findByAccessKeyAndExpireDateGreaterThanEqual("default", today)
                .orElseThrow();

        // then
        assertThat(found.getIntro()).isEqualTo(home.getIntro());
        assertThat(found.getQuestions()).hasSize(2);
    }

    @Test
    @DisplayName("만료된 홈 화면 객체를 조회하면 빈 Optional 객체가 반환됩니다.")
    void findByAccessKeyExpired() {
        // given
        LocalDate today = LocalDate.of(2025, 1, 1);

        Home home = createHome("access-key", LocalDate.of(2024, 12, 31));

        // when
        homeRepository.save(home);
        Optional<Home> found = homeRepository.findByAccessKeyAndExpireDateGreaterThanEqual(
                "access-key",
                today
        );

        // then
        assertThat(found.isEmpty()).isTrue();
    }

    private Home createHome(String accessKey, LocalDate expireDate) {
        Home home = new Home();
        home.setAccessKey(accessKey);
        home.setInterests(List.of("Spring", "추천시스템"));
        home.setIntro("안녕하세요!");
        home.setQuestions(List.of(
                new Question(1, "질문?", "대답"),
                new Question(2, "질문?", "대답")
        ));
        home.setExpireDate(expireDate);
        return home;
    }
}