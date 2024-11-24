package net.studio1122.changi1122.portfoliowebsite.domain.home;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Document("home")
public class Home {

    @Id
    private String id;
    private String accessKey;
    private LocalDate expireDate;

    private List<String> interests;
    private String intro;
    private List<Question> questions;

    // TODO: 프로젝트 추가
}
