package net.studio1122.changi1122.portfoliowebsite.domain.resume;

import net.studio1122.changi1122.portfoliowebsite.domain.home.entity.Question;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.entity.Resume;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.entity.record.Career;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.entity.record.Keyword;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.entity.record.Record;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.entity.record.SideProject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
class ResumeServiceTest {

    @Autowired
    ResumeRepository resumeRepository;

    @Autowired
    ResumeService resumeService;

    @AfterEach
    void tearDown() {
        resumeRepository.deleteAll();
    }

    @DisplayName("새로운 이력서 객체를 추가합니다.")
    @Test
    void createResume() {
        //given
        Resume request = createResume("default", LocalDate.of(2025, 12, 31));

        // when
        request = resumeService.createResume(request);

        // then
        Resume saved = resumeRepository.findById(request.getId()).orElseThrow();

        assertThat(saved.getExpireDate()).isEqualTo(request.getExpireDate());
        assertThat(saved.getLinks()).hasSize(2);
        assertThat(saved.getIntro()).isEqualTo(request.getIntro());
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

    @DisplayName("중복된 accessKey를 가진 이력서 객체를 추가하면 오류가 반환됩니다.")
    @Test
    void createResumeNotUniqueAccessKey() {
        //given
        Resume request1 = createResume("default", LocalDate.of(2025, 12, 31));
        Resume request2 = createResume("default", LocalDate.of(2024, 12, 30));

        // when
        resumeService.createResume(request1);

        // then
        assertThatThrownBy(() -> resumeService.createResume(request2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("accessKey가 동일한 이력서 객체가 존재합니다.");
    }

    @DisplayName("이력서 객체를 수정합니다.")
    @Test
    void updateResume() {
        //given
        Resume resume = createResume("default", LocalDate.of(2025, 12, 31));
        resumeRepository.save(resume);

        Resume request = getUpdatedResume(
                "default",
                LocalDate.of(2020, 1, 1)
        );

        // when
        request = resumeService.updateResume(request.getAccessKey(), request);

        // then
        Resume saved = resumeRepository.findById(request.getId()).orElseThrow();
        assertThat(saved.getExpireDate()).isEqualTo(request.getExpireDate());
        assertThat(saved.getLinks()).hasSize(1);
        assertThat(saved.getIntro()).isEqualTo(request.getIntro());
        assertThat(saved.getInterests()).hasSize(1)
                .containsExactlyInAnyOrder("Spring");
        assertThat(saved.getQuestions()).hasSize(1)
                .extracting("question", "answer")
                .containsExactlyInAnyOrder(
                        tuple("질문?", "대답")
                );
        assertThat(saved.getExperiences()).hasSize(1)
                .extracting("name")
                .contains("수정된경력");
        assertThat(saved.getDegrees()).hasSize(1)
                .extracting("name")
                .contains("수정된학력");
        assertThat(saved.getProjects()).hasSize(1)
                .extracting("name")
                .contains("수정된프로젝트");
        assertThat(saved.getCertifications()).hasSize(1)
                .extracting("name")
                .contains("수정된자격증");
        assertThat(saved.getAwards()).hasSize(1)
                .extracting("name")
                .contains("수정된수상");
        assertThat(saved.getCourseCertifications()).hasSize(1)
                .extracting("name")
                .contains("수정된수료증");
    }

    @DisplayName("존재하지 않는 accessKey로 이력서 객체를 수정하면 오류가 반환됩니다.")
    @Test
    void updateResumeNoSuchAccessKey() {
        //given
        Resume request = createResume("no-such-key", LocalDate.of(2025, 12, 31));

        // when then
        assertThatThrownBy(() -> resumeService.updateResume(request.getAccessKey(), request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("accessKey로 이력서 객체를 조회합니다.")
    @Test
    void readResume() {
        //given
        LocalDate today = LocalDate.of(2025, 12, 31);
        Resume resume = createResume("default", LocalDate.of(2025, 12, 31));
        resumeRepository.save(resume);


        // when
        Resume found = resumeService.readResume("default", today);

        // then
        assertThat(found.getExpireDate()).isEqualTo(resume.getExpireDate());
        assertThat(found.getLinks()).hasSize(2);
        assertThat(found.getIntro()).isEqualTo(resume.getIntro());
        assertThat(found.getInterests()).hasSize(2)
                .containsExactlyInAnyOrder("Spring", "추천시스템");
        assertThat(found.getQuestions()).hasSize(2)
                .extracting("question", "answer")
                .containsExactlyInAnyOrder(
                        tuple("질문?", "대답"),
                        tuple("질문2?", "대답")
                );
        assertThat(found.getExperiences()).hasSize(1)
                .extracting("name")
                .contains("테스트경력");
        assertThat(found.getDegrees()).hasSize(1)
                .extracting("name")
                .contains("테스트학력");
        assertThat(found.getProjects()).hasSize(1)
                .extracting("name")
                .contains("사이드프로젝트");
        assertThat(found.getCertifications()).hasSize(1)
                .extracting("name")
                .contains("자격증");
        assertThat(found.getAwards()).hasSize(1)
                .extracting("name")
                .contains("수상");
        assertThat(found.getCourseCertifications()).hasSize(1)
                .extracting("name")
                .contains("수료증");
    }

    @DisplayName("만료된 이력서 객체를 조회하면 오류가 반환됩니다.")
    @Test
    void readResumeExpired() {
        //given
        LocalDate today = LocalDate.of(2025, 12, 31);
        Resume resume = createResume("default", LocalDate.of(2025, 12, 30));
        resumeRepository.save(resume);

        // when then
        assertThatThrownBy(() -> resumeService.readResume("default", today))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효 기한이 만료되었습니다.");
    }

    @DisplayName("내부에서 현재 날짜에 상관 없이 accessKey로 이력서 객체를 조회합니다.")
    @Test
    void readResumeWithoutDate() {
        //given
        Resume resume = createResume("default", LocalDate.of(2025, 12, 31));
        resumeRepository.save(resume);

        // when
        Resume found = resumeService.readResume("default");

        // then
        assertThat(found.getExpireDate()).isEqualTo(resume.getExpireDate());
        assertThat(found.getLinks()).hasSize(2);
        assertThat(found.getIntro()).isEqualTo(resume.getIntro());
        assertThat(found.getInterests()).hasSize(2)
                .containsExactlyInAnyOrder("Spring", "추천시스템");
        assertThat(found.getQuestions()).hasSize(2)
                .extracting("question", "answer")
                .containsExactlyInAnyOrder(
                        tuple("질문?", "대답"),
                        tuple("질문2?", "대답")
                );
        assertThat(found.getExperiences()).hasSize(1)
                .extracting("name")
                .contains("테스트경력");
        assertThat(found.getDegrees()).hasSize(1)
                .extracting("name")
                .contains("테스트학력");
        assertThat(found.getProjects()).hasSize(1)
                .extracting("name")
                .contains("사이드프로젝트");
        assertThat(found.getCertifications()).hasSize(1)
                .extracting("name")
                .contains("자격증");
        assertThat(found.getAwards()).hasSize(1)
                .extracting("name")
                .contains("수상");
        assertThat(found.getCourseCertifications()).hasSize(1)
                .extracting("name")
                .contains("수료증");
    }

    @DisplayName("존재하지 않는 accessKey로 이력서 객체를 조회하면 오류가 반환됩니다.")
    @Test
    void readResumeNoSuchAccessKey() {
        //given
        LocalDate today = LocalDate.of(2024, 12, 31);
        Resume request = createResume("no-such-key", LocalDate.of(2025, 12, 31));

        // when then
        assertThatThrownBy(() -> resumeService.readResume(request.getAccessKey(), today))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("accessKey로 이력서 객체를 삭제합니다.")
    @Test
    void deleteResume() {
        //given
        Resume resume = createResume("default", LocalDate.of(2025, 12, 30));
        resumeRepository.save(resume);

        // when
        resumeService.deleteResume(resume.getAccessKey());

        // then
        assertThat(resumeRepository.existsByAccessKey(resume.getAccessKey())).isFalse();
    }

    @DisplayName("존재하지 않는 accessKey로 이력서 객체를 삭제하면 오류가 반환됩니다.")
    @Test
    void deleteResumeNoSuchAccessKey() {
        //given
        Resume request = createResume("no-such-key", LocalDate.of(2025, 12, 31));

        // when then
        assertThatThrownBy(() -> resumeService.deleteResume(request.getAccessKey()))
                .isInstanceOf(NoSuchElementException.class);
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

    private Resume getUpdatedResume(String accessKey, LocalDate expireDate) {
        return Resume.builder()
                .accessKey(accessKey)
                .expireDate(expireDate)
                .links(List.of("mailto:test@test.com"))
                .interests(List.of("Spring"))
                .intro("안녕히가세요!")
                .questions(List.of(
                        new Question(1, "질문?", "대답")
                ))
                .experiences(List.of(
                        Career.builder()
                                .duration("2025-03 ~")
                                .name("수정된경력")
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
                                .name("수정된학력")
                                .position("테스트학위")
                                .themeColor("#B5285B")
                                .build()
                ))
                .projects(List.of(
                        SideProject.builder()
                                .duration("2022-09")
                                .name("수정된프로젝트")
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
                                .name("수정된자격증")
                                .build()
                ))
                .awards(List.of(
                        Record.builder()
                                .duration("2024-07")
                                .name("수정된수상")
                                .links(List.of("<a>증빙</a>"))
                                .build()
                ))
                .courseCertifications(List.of(
                        Record.builder()
                                .duration("2024-12")
                                .name("수정된수료증")
                                .links(List.of("<a>증빙</a>"))
                                .build()
                ))
                .build();
    }
}