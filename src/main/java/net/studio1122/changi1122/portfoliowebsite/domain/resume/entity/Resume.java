package net.studio1122.changi1122.portfoliowebsite.domain.resume.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.studio1122.changi1122.portfoliowebsite.domain.home.entity.Question;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.entity.record.Career;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.entity.record.Record;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.entity.record.SideProject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document("resume")
public class Resume {

    @Id
    private String id;
    @Indexed(unique = true)
    private String accessKey;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expireDate;

    @NotEmpty
    private List<String> links;
    @NotEmpty
    private List<String> interests;

    @NotBlank
    private String intro;
    @NotEmpty
    private List<Question> questions;

    private List<Career> experiences;
    private List<Career> degrees;
    private List<SideProject> projects;
    private List<String> projectIds;        // 프로젝트 페이지에 표시될 프로젝트 목록
    private List<Record> certifications;
    private List<Record> awards;
    private List<Record> courseCertifications;

    public String getExpireDateString() {
        return (expireDate != null) ? expireDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) : "";
    }

    public boolean isExpired(LocalDate today) {
        return expireDate.isBefore(today);
    }

    /** 관리 폼 textarea용: 프로젝트 ID를 한 줄에 하나씩 표시합니다. */
    public String getProjectIdsText() {
        return (projectIds != null) ? String.join("\n", projectIds) : "";
    }

    /** 관리 폼 textarea용: 줄 단위로 나누고, 공백과 빈 줄, 중복을 제거합니다. */
    public void setProjectIdsText(String text) {
        this.projectIds = (text == null) ? List.of() : text.lines()
                .map(String::strip)
                .filter(line -> !line.isEmpty())
                .distinct()
                .toList();
    }

    @Builder
    private Resume(String accessKey, LocalDate expireDate, List<String> links, List<String> interests, String intro,
                  List<Question> questions, List<Career> experiences, List<Career> degrees, List<SideProject> projects,
                  List<String> projectIds, List<Record> certifications, List<Record> awards, List<Record> courseCertifications) {
        this.accessKey = accessKey;
        this.expireDate = expireDate;
        this.links = links;
        this.interests = interests;
        this.intro = intro;
        this.questions = questions;
        this.experiences = experiences;
        this.degrees = degrees;
        this.projects = projects;
        this.projectIds = projectIds;
        this.certifications = certifications;
        this.awards = awards;
        this.courseCertifications = courseCertifications;
    }
}
