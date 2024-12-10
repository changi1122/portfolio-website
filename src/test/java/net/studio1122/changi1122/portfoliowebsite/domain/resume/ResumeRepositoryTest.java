package net.studio1122.changi1122.portfoliowebsite.domain.resume;

import net.studio1122.changi1122.portfoliowebsite.domain.home.Home;
import net.studio1122.changi1122.portfoliowebsite.domain.home.Question;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.record.Career;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.record.Keyword;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.record.Record;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.record.SideProject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
class ResumeRepositoryTest {

    @Autowired
    ResumeRepository resumeRepository;

    @AfterEach
    void tearDown() {
        resumeRepository.deleteAll();
    }

    @Test
    @DisplayName("이력서 객체를 accessKey로 조회합니다.")
    void findByAccessKey() {
        // given
        LocalDate today = LocalDate.of(2024, 12, 31);
        Resume resume = createResume("default", LocalDate.of(2024, 12, 31));

        // when
        resumeRepository.save(resume);
        Resume saved = resumeRepository.findByAccessKey("default").orElseThrow();

        // then
        assertThat(saved.getExpireDate()).isEqualTo(resume.getExpireDate());
        assertThat(saved.getLinks()).hasSize(2);
        assertThat(saved.getIntro()).isEqualTo(resume.getIntro());
        assertThat(saved.getInterests()).hasSize(2)
                .containsExactlyInAnyOrder("Spring", "추천시스템");
        assertThat(saved.getQuestions()).hasSize(2)
                .extracting("question", "answer")
                .containsExactlyInAnyOrder(
                        tuple("질문?", "대답"),
                        tuple("질문2?", "대답")
                );
        assertThat(saved.getExperiences()).hasSize(1)
                .extracting("name")
                .contains("테스트경력");
        assertThat(saved.getDegrees()).hasSize(1)
                .extracting("name")
                .contains("테스트학력");
        assertThat(saved.getProjects()).hasSize(1)
                .extracting("name")
                .contains("사이드프로젝트");
        assertThat(saved.getCertifications()).hasSize(1)
                .extracting("name")
                .contains("자격증");
        assertThat(saved.getAwards()).hasSize(1)
                .extracting("name")
                .contains("수상");
        assertThat(saved.getCourseCertifications()).hasSize(1)
                .extracting("name")
                .contains("수료증");

    }

    @DisplayName("이력서 객체를 accessKey로 삭제합니다.")
    @Test
    void deleteByAccessKey() {
        //given
        Resume resume = createResume("default", LocalDate.of(2024, 12, 31));
        resumeRepository.save(resume);

        // when
        resumeRepository.deleteByAccessKey(resume.getAccessKey());

        // then
        Optional<Resume> resumeOptional = resumeRepository.findByAccessKey(resume.getAccessKey());
        assertThat(resumeOptional.isEmpty()).isTrue();
    }

    private Resume createResume(String accessKey, LocalDate expireDate) {
        return Resume.builder()
                .accessKey(accessKey)
                .expireDate(expireDate)
                .links(List.of("mailto:test@test.com", "https://github.com/changi1122"))
                .interests(List.of("Spring", "추천시스템"))
                .intro("안녕하세요!")
                .questions(List.of(
                        new Question(1, "질문?", "대답"),
                        new Question(2, "질문2?", "대답")
                ))
                .experiences(List.of(
                        Career.builder()
                                .duration("2025-03 ~")
                                .name("테스트경력")
                                .position("포지션")
                                .description("설명")
                                .tags(List.of("Spring", "Cloud"))
                                .links(Collections.emptyList())
                                .themeColor("#00CAAD")
                                .build()
                ))
                .degrees(List.of(
                        Career.builder()
                                .duration("2019-03 ~ 2025-02")
                                .name("테스트학력")
                                .position("테스트학위")
                                .themeColor("#B5285B")
                                .build()
                ))
                .projects(List.of(
                        SideProject.builder()
                                .duration("2022-09")
                                .name("사이드프로젝트")
                                .description("주제")
                                .keywords(List.of(
                                        new Keyword("기술", "Spring, JPA"),
                                        new Keyword("역할", "Backend 개발")
                                ))
                                .links(List.of("<a>프로젝트 소개</a>"))
                                .themeColor("#9800FF")
                                .build()
                ))
                .certifications(List.of(
                        Record.builder()
                                .duration("2024-09")
                                .name("자격증")
                                .build()
                ))
                .awards(List.of(
                        Record.builder()
                                .duration("2024-07")
                                .name("수상")
                                .links(List.of("<a>증빙</a>"))
                                .build()
                ))
                .courseCertifications(List.of(
                        Record.builder()
                                .duration("2024-12")
                                .name("수료증")
                                .links(List.of("<a>증빙</a>"))
                                .build()
                ))
                .build();
    }
}