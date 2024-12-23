package net.studio1122.changi1122.portfoliowebsite.domain.home;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document("home")
public class Home {

    @Id
    private String id;
    @Indexed(unique = true)
    private String accessKey;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate expireDate;

    @NotEmpty
    private List<String> interests;
    @NotBlank
    private String intro;
    @NotEmpty
    private List<Question> questions;

    // TODO: 프로젝트 추가

    @Builder
    public Home(String accessKey, LocalDate expireDate, List<String> interests, String intro, List<Question> questions)
    {
        this.accessKey = accessKey;
        this.expireDate = expireDate;
        this.interests = interests;
        this.intro = intro;
        this.questions = questions;
    }
}
