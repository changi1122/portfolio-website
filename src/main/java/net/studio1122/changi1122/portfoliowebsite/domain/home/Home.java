package net.studio1122.changi1122.portfoliowebsite.domain.home;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
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
@Document("home")
public class Home {

    @Id
    private String id;
    @Indexed(unique = true)
    private String accessKey;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expireDate;

    @NotEmpty
    private List<String> interests;
    @NotBlank
    private String intro;
    @NotEmpty
    private List<Question> questions;

    @Size(min = 3, max = 3)
    private List<SideProjectImage> projects;

    public String getExpireDateString() {
        return (expireDate != null) ? expireDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) : "";
    }

    @Builder
    public Home(String accessKey, LocalDate expireDate, List<String> interests, String intro, List<Question> questions,
                List<SideProjectImage> projects)
    {
        this.accessKey = accessKey;
        this.expireDate = expireDate;
        this.interests = interests;
        this.intro = intro;
        this.questions = questions;
        this.projects = projects;
    }
}
