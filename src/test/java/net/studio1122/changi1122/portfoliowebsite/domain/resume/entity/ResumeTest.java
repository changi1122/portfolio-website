package net.studio1122.changi1122.portfoliowebsite.domain.resume.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ResumeTest {

    @DisplayName("프로젝트 ID 텍스트를 줄 단위로 나누고, 공백·빈 줄·중복을 제거합니다.")
    @Test
    void setProjectIdsText() {
        // given
        Resume resume = new Resume();

        // when
        resume.setProjectIdsText(" id1 \r\n\nid2\nid1\n");

        // then
        assertThat(resume.getProjectIds()).containsExactly("id1", "id2");
        assertThat(resume.getProjectIdsText()).isEqualTo("id1\nid2");
    }

    @DisplayName("프로젝트 ID 텍스트가 null이면 빈 목록으로 설정합니다.")
    @Test
    void setProjectIdsTextNull() {
        // given
        Resume resume = new Resume();

        // when
        resume.setProjectIdsText(null);

        // then
        assertThat(resume.getProjectIds()).isEmpty();
        assertThat(resume.getProjectIdsText()).isEmpty();
    }

    @DisplayName("프로젝트 ID 목록이 없으면 빈 텍스트를 반환합니다.")
    @Test
    void getProjectIdsTextWithoutIds() {
        // given
        Resume resume = new Resume();
        resume.setProjectIds(null);

        // when then
        assertThat(resume.getProjectIdsText()).isEmpty();
    }

    @DisplayName("프로젝트 ID 목록을 한 줄에 하나씩 텍스트로 반환합니다.")
    @Test
    void getProjectIdsText() {
        // given
        Resume resume = new Resume();
        resume.setProjectIds(List.of("id1", "id2", "id3"));

        // when then
        assertThat(resume.getProjectIdsText()).isEqualTo("id1\nid2\nid3");
    }
}
