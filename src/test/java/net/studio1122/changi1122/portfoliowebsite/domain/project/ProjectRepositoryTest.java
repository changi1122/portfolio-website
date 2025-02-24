package net.studio1122.changi1122.portfoliowebsite.domain.project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @AfterEach
    void tearDown() {
        projectRepository.deleteAll();
    }

    @DisplayName("프로젝트 이름으로 프로젝트 객체를 조회합니다.")
    @Test
    void findByName() {
        // given
        Project project = createProject("이름");
        project = projectRepository.save(project);

        // when
        Project found = projectRepository.findByName(project.getName()).orElseThrow();

        // then
        assertThat(found.getId()).isEqualTo(project.getId());
    }

    @DisplayName("전체 프로젝트 목록을 조회합니다.")
    @Test
    void findAllBy() {
        // given
        Project project1 = projectRepository.save(createProject("프로젝트1"));
        Project project2 = projectRepository.save(createProject("프로젝트2"));

        PageRequest pageRequest = PageRequest.of(0, 4);

        // when
        Page<Project> result = projectRepository.findAllBy(pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2)
                .extracting("name")
                .containsExactlyInAnyOrder("프로젝트1", "프로젝트2");
    }

    @DisplayName("공개 상태인 프로젝트 목록을 조회합니다.")
    @Test
    void findByHiddenFalse() {
        // given
        Project project1 = projectRepository.save(createProject("프로젝트1"));
        Project project2 = projectRepository.save(createProject("프로젝트2"));
        project2.setHidden(true);
        projectRepository.save(project2);

        PageRequest pageRequest = PageRequest.of(0, 4);

        // when
        List<Project> result = projectRepository.findByIsHiddenFalse();

        // then
        assertThat(result).hasSize(1)
                .extracting("name")
                .containsExactlyInAnyOrder("프로젝트1");
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
                .body("markdown")
                .bodyLinks(List.of("<a>소개</a>"))
                .themeColor("#000000")
                .build();
    }
}