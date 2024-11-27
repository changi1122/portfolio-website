package net.studio1122.changi1122.portfoliowebsite.web.home;

import net.studio1122.changi1122.portfoliowebsite.domain.home.Home;
import net.studio1122.changi1122.portfoliowebsite.domain.home.HomeRepository;
import net.studio1122.changi1122.portfoliowebsite.domain.home.Question;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HomeServiceTest {

    @Autowired
    HomeRepository homeRepository;
    @Autowired
    HomeService homeService;

    @AfterEach
    void tearDown() {
        homeRepository.deleteAll();
    }

    @DisplayName("새로운 홈화면 객체를 추가합니다.")
    @Test
    void createHome() {
        //given
        Home request = createHome("default", LocalDate.of(2025, 12, 31));

        // when
        request = homeService.createHome(request);

        // then
        Home saved = homeRepository.findById(request.getId()).orElseThrow();

        assertThat(saved.getAccessKey()).isEqualTo(request.getAccessKey());
        assertThat(saved.getInterests()).hasSize(2)
                .containsExactlyInAnyOrder("Spring", "추천시스템");
        assertThat(saved.getIntro()).isEqualTo(request.getIntro());
        assertThat(saved.getQuestions()).hasSize(2)
                .extracting("question", "answer")
                .containsExactlyInAnyOrder(
                        tuple("질문?", "대답"),
                        tuple("질문2?", "대답")
                );
        assertThat(saved.getExpireDate()).isEqualTo(request.getExpireDate());
    }

    @DisplayName("중복된 accessKey를 가진 홈화면 객체를 추가하면 오류가 반환됩니다.")
    @Test
    void createHomeNotUniqueAccessKey() {
        //given
        Home request1 = createHome("default", LocalDate.of(2025, 12, 31));
        Home request2 = createHome("default", LocalDate.of(2024, 12, 30));

        // when
        homeService.createHome(request1);

        // then
        assertThatThrownBy(() -> homeService.createHome(request2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("accessKey가 동일한 홈화면 객체가 존재합니다.");
    }

    @DisplayName("홈화면 객체를 수정합니다.")
    @Test
    void updateHome() {
        //given
        Home home = createHome("default", LocalDate.of(2025, 12, 31));
        homeRepository.save(home);

        Home request = createHome(
                "default",
                List.of("추천시스템"),
                "안녕히가세요!",
                List.of(new Question(1, "질문?", "대답")),
                LocalDate.of(2020, 1, 1)
        );

        // when
        request = homeService.updateHome(request.getAccessKey(), request);

        // then
        Home saved = homeRepository.findById(request.getId()).orElseThrow();
        assertThat(saved.getIntro()).isEqualTo(request.getIntro());
        assertThat(saved.getInterests()).hasSize(1)
                .containsExactlyInAnyOrder("추천시스템");
        assertThat(saved.getQuestions()).hasSize(1)
                .extracting("question", "answer")
                .containsExactlyInAnyOrder(
                        tuple("질문?", "대답")
                );
        assertThat(saved.getExpireDate()).isEqualTo(request.getExpireDate());
    }

    @DisplayName("존재하지 않는 accessKey로 홈화면 객체를 수정하면 오류가 반환됩니다.")
    @Test
    void updateHomeNoSuchAccessKey() {
        //given
        Home request = createHome("no-such-key", LocalDate.of(2025, 12, 31));

        // when then
        assertThatThrownBy(() -> homeService.updateHome(request.getAccessKey(), request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("accessKey로 홈화면 객체를 조회합니다.")
    @Test
    void readHome() {
        //given
        LocalDate today = LocalDate.of(2025, 12, 31);
        Home home = createHome("default", LocalDate.of(2025, 12, 31));
        homeRepository.save(home);

        // when
        Home found = homeService.readHome("default", today);

        // then
        assertThat(found.getAccessKey()).isEqualTo(home.getAccessKey());
        assertThat(found.getInterests()).hasSize(2)
                .containsExactlyInAnyOrder("Spring", "추천시스템");
        assertThat(found.getIntro()).isEqualTo(home.getIntro());
        assertThat(found.getQuestions()).hasSize(2)
                .extracting("question", "answer")
                .containsExactlyInAnyOrder(
                        tuple("질문?", "대답"),
                        tuple("질문2?", "대답")
                );
        assertThat(found.getExpireDate()).isEqualTo(home.getExpireDate());
    }

    @DisplayName("만료된 홈화면 객체를 조회하면 오류가 반환됩니다.")
    @Test
    void readHomeExpired() {
        //given
        LocalDate today = LocalDate.of(2025, 12, 31);
        Home home = createHome("default", LocalDate.of(2025, 12, 30));
        homeRepository.save(home);

        // when then
        assertThatThrownBy(() -> homeService.readHome("default", today))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효 기한이 만료되었습니다.");
    }

    @DisplayName("존재하지 않는 accessKey로 홈화면 객체를 조회하면 오류가 반환됩니다.")
    @Test
    void readHomeNoSuchAccessKey() {
        //given
        Home request = createHome("no-such-key", LocalDate.of(2025, 12, 31));

        // when then
        assertThatThrownBy(() -> homeService.updateHome(request.getAccessKey(), request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("accessKey로 홈화면 객체를 삭제합니다.")
    @Test
    void deleteHome() {
        //given
        Home home = createHome("default", LocalDate.of(2025, 12, 30));
        homeRepository.save(home);

        // when
        homeService.deleteHome(home.getAccessKey());

        // then
        assertThat(homeRepository.existsByAccessKey(home.getAccessKey())).isFalse();
    }

    @DisplayName("존재하지 않는 accessKey로 홈화면 객체를 삭제하면 오류가 반환됩니다.")
    @Test
    void deleteHomeNoSuchAccessKey() {
        //given
        Home request = createHome("no-such-key", LocalDate.of(2025, 12, 31));

        // when then
        assertThatThrownBy(() -> homeService.deleteHome(request.getAccessKey()))
                .isInstanceOf(NoSuchElementException.class);
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

    private Home createHome(String accessKey, List<String> interests, String intro, List<Question> questions,
                            LocalDate expireDate) {
        return Home.builder()
                .accessKey(accessKey)
                .interests(interests)
                .intro(intro)
                .questions(questions)
                .expireDate(expireDate)
                .build();
    }
}