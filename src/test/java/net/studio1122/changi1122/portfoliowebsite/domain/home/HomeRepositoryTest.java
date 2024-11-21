package net.studio1122.changi1122.portfoliowebsite.domain.home;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HomeRepositoryTest {

    @Autowired
    HomeRepository homeRepository;

    @AfterEach
    void clear() {
        homeRepository.deleteAll();
    }

    @Test
    @DisplayName("홈 화면을 생성하고 AccessKey로 조회합니다.")
    void findByAccessKey() {
        // given
        Home home = new Home();
        home.setAccessKey("default");
        home.setInterests(List.of("Spring", "추천시스템"));
        home.setIntro("안녕하세요!");
        home.setQuestions(List.of(
                new Question(1, "질문?", "대답"),
                new Question(2, "질문?", "대답")
        ));

        // when
        homeRepository.save(home);

        // then
        Home found = homeRepository.findByAccessKey("default");
        assertThat(found.getIntro()).isEqualTo(home.getIntro());
    }
}