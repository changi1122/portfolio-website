package net.studio1122.changi1122.portfoliowebsite.domain.home;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private LocalDate expireDate;

    private List<String> interests;
    private String intro;
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
