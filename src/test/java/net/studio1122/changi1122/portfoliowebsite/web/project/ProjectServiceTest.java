package net.studio1122.changi1122.portfoliowebsite.web.project;

import net.studio1122.changi1122.portfoliowebsite.domain.project.Keyword;
import net.studio1122.changi1122.portfoliowebsite.domain.project.Project;
import net.studio1122.changi1122.portfoliowebsite.domain.project.ProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectService projectService;

    @AfterEach
    void tearDown() {
        projectRepository.deleteAll();
    }

    @DisplayName("새로운 프로젝트를 추가합니다.")
    @Test
    void createProject() {
        // given
        Project request = createProject("프로젝트");

        // when
        request = projectService.createProject(request);

        // then
        Project saved = projectRepository.findByName(request.getName()).orElseThrow();

        assertThat(saved)
                .extracting("name", "description", "body", "isHidden")
                .containsExactly(
                        "프로젝트", "주제", "본문.MD", false
                );
    }

    @DisplayName("프로젝트를 수정합니다.")
    @Test
    void updateProject() {
        // given
        Project project = createProject("프로젝트");
        project = projectRepository.save(project);

        Project request = Project.builder()
                .name("프로젝트수정")
                .description("주제수정")
                .imageSrc("/수정")
                .keywords(List.of(
                        new Keyword("기술수정", "Spring")
                ))
                .links(List.of("<a>소개</a>", "<a>웹페이지</a>"))
                .category("카테고리수정")
                .badges(List.of("Spring boot"))
                .body("본문수정.MD")
                .bodyLinks(Collections.emptyList())
                .build();

        // when
        request = projectService.updateProject(project.getId(), request);

        // then
        Project saved = projectRepository.findByName(request.getName()).orElseThrow();

        assertThat(saved)
                .extracting("name", "description", "imageSrc", "category", "body")
                .containsExactly("프로젝트수정", "주제수정", "/수정", "카테고리수정", "본문수정.MD");
        assertThat(saved.getKeywords()).hasSize(1);
        assertThat(saved.getLinks()).hasSize(2);
        assertThat(saved.getBadges()).hasSize(1)
                .containsExactlyInAnyOrder("Spring boot");
        assertThat(saved.getBodyLinks()).isEmpty();
    }

    @DisplayName("존재하지 않는 id로 프로젝트을 수정하면 오류가 반환됩니다.")
    @Test
    void updateProjectNoSuchId() {
        //given
        Project request = createProject("프로젝트");

        // when then
        assertThatThrownBy(() -> projectService.updateProject("no-such-id", request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("프로젝트 표시 여부를 변경합니다.")
    @CsvSource({"false", "true"})
    @ParameterizedTest
    void toggleProjectVisibility(boolean isHidden) {
        // given
        Project request = createProject("프로젝트");
        request.setHidden(isHidden);
        request = projectRepository.save(request);

        // when
        projectService.toggleProjectVisibility(request.getId());

        // then
        Project saved = projectRepository.findByName(request.getName()).orElseThrow();
        assertThat(saved.isHidden()).isEqualTo(!isHidden);
    }

    @DisplayName("프로젝트를 삭제합니다.")
    @Test
    void deleteProject() {
        // given
        Project request = createProject("프로젝트");
        request = projectRepository.save(request);

        // when
        projectService.deleteProject(request.getId());

        // then
        assertThat(projectRepository.findById(request.getId()).isEmpty()).isTrue();
    }

    @DisplayName("존재하지 않는 id로 프로젝트을 삭제하면 오류가 반환됩니다.")
    @Test
    void deleteProjectNoSuchId() {
        // when then
        assertThatThrownBy(() -> projectService.deleteProject("no-such-id"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("프로젝트 상세 페이지를 조회합니다.")
    @Test
    void readProject() {
        // given
        Project request = createProject("프로젝트");
        request = projectRepository.save(request);

        // when
        Project project = projectService.readProject(request.getId());

        // then
        assertThat(project.getName()).isEqualTo(request.getName());
    }

    @DisplayName("존재하지 않는 id로 프로젝트을 조회하면 오류가 반환됩니다.")
    @Test
    void readProjectNoSuchId() {
        // when then
        assertThatThrownBy(() -> projectService.readProject("no-such-id"))
                .isInstanceOf(NoSuchElementException.class);
    }

    private Project createProject(String name) {
        return Project.builder()
                .name(name)
                .description("주제")
                .imageSrc("/")
                .keywords(List.of(
                        new Keyword("기술", "Spring")
                ))
                .links(List.of("<a>소개</a>"))
                .category("이름 위 표시 문자열")
                .badges(List.of("Spring", "React"))
                .body("본문.MD")
                .bodyLinks(List.of("<a>소개</a>"))
                .build();
    }
}