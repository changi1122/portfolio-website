package net.studio1122.changi1122.portfoliowebsite.domain.project;

import net.studio1122.changi1122.portfoliowebsite.domain.project.entity.Keyword;
import net.studio1122.changi1122.portfoliowebsite.domain.project.entity.Project;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
                .bodyHtml("<p>본문수정.MD</p>")
                .bodyLinks(Collections.emptyList())
                .order(1)
                .themeColor("#ffffff")
                .build();

        // when
        request = projectService.updateProject(project.getId(), request);

        // then
        Project saved = projectRepository.findByName(request.getName()).orElseThrow();

        assertThat(saved)
                .extracting("name", "description", "imageSrc", "category", "body", "bodyHtml", "order", "themeColor")
                .containsExactly("프로젝트수정", "주제수정", "/수정", "카테고리수정", "본문수정.MD", "<p>본문수정.MD</p>", 1, "#ffffff");
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

    @DisplayName("지정한 ID의 프로젝트만 프로젝트 표시 순서(order)대로 조회합니다.")
    @Test
    void listProjectVisibleWithIds() {
        // given
        Project a = projectRepository.save(createProject("A", 3));
        Project b = projectRepository.save(createProject("B", 1));
        projectRepository.save(createProject("C", 2));

        // when
        List<Project> projects = projectService.listProjectVisible(List.of(a.getId(), b.getId()));

        // then
        assertThat(projects).extracting("name")
                .containsExactly("B", "A");
    }

    @DisplayName("ID 목록이 비어 있으면 공개 프로젝트 전체를 조회합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void listProjectVisibleWithoutIds(List<String> ids) {
        // given
        projectRepository.save(createProject("A", 2));
        projectRepository.save(createProject("B", 1));
        Project hidden = createProject("숨김", 0);
        hidden.setHidden(true);
        projectRepository.save(hidden);

        // when
        List<Project> projects = projectService.listProjectVisible(ids);

        // then
        assertThat(projects).extracting("name")
                .containsExactly("B", "A");
    }

    @DisplayName("존재하지 않는 ID는 제외하고 조회합니다.")
    @Test
    void listProjectVisibleWithUnknownIds() {
        // given
        Project a = projectRepository.save(createProject("A", 1));
        projectRepository.save(createProject("B", 2));

        // when
        List<Project> projects = projectService.listProjectVisible(List.of("no-such-id", a.getId()));

        // then
        assertThat(projects).extracting("name")
                .containsExactly("A");
    }

    @DisplayName("유효한 ID가 하나도 없으면 공개 프로젝트 전체를 조회합니다.")
    @Test
    void listProjectVisibleWithOnlyUnknownIds() {
        // given
        projectRepository.save(createProject("A", 2));
        projectRepository.save(createProject("B", 1));

        // when
        List<Project> projects = projectService.listProjectVisible(List.of("no-such-id"));

        // then
        assertThat(projects).extracting("name")
                .containsExactly("B", "A");
    }

    @DisplayName("ID를 지정하면 비공개 프로젝트도 조회합니다.")
    @Test
    void listProjectVisibleWithHiddenId() {
        // given
        Project hidden = createProject("숨김", 1);
        hidden.setHidden(true);
        hidden = projectRepository.save(hidden);
        projectRepository.save(createProject("공개", 2));

        // when
        List<Project> projects = projectService.listProjectVisible(List.of(hidden.getId()));

        // then
        assertThat(projects).extracting("name")
                .containsExactly("숨김");
    }

    private Project createProject(String name, int order) {
        Project project = createProject(name);
        project.setOrder(order);
        return project;
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
                .themeColor("#000000")
                .build();
    }
}