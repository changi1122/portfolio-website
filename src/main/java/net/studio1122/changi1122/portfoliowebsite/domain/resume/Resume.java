package net.studio1122.changi1122.portfoliowebsite.domain.resume;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.studio1122.changi1122.portfoliowebsite.domain.home.Question;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.record.Career;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.record.Record;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.record.SideProject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document("resume")
public class Resume {

    @Id
    private String id;
    @Indexed(unique = true)
    private String accessKey;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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
    private List<Record> certifications;
    private List<Record> awards;
    private List<Record> courseCertifications;


    @Builder
    private Resume(String accessKey, LocalDate expireDate, List<String> links, List<String> interests, String intro,
                  List<Question> questions, List<Career> experiences, List<Career> degrees, List<SideProject> projects,
                  List<Record> certifications, List<Record> awards, List<Record> courseCertifications) {
        this.accessKey = accessKey;
        this.expireDate = expireDate;
        this.links = links;
        this.interests = interests;
        this.intro = intro;
        this.questions = questions;
        this.experiences = experiences;
        this.degrees = degrees;
        this.projects = projects;
        this.certifications = certifications;
        this.awards = awards;
        this.courseCertifications = courseCertifications;
    }
}
