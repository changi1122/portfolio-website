package net.studio1122.changi1122.portfoliowebsite.domain.project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        Project project = createProject();
        project = projectRepository.save(project);

        // when
        Project found = projectRepository.findByName(project.getName()).orElseThrow();

        // then
        assertThat(found.getId()).isEqualTo(project.getId());
    }

    private Project createProject() {
        return Project.builder()
                .name("이름")
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
                .build();
    }


}