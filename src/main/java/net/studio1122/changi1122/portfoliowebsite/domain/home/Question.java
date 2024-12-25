package net.studio1122.changi1122.portfoliowebsite.domain.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private Integer questionNumber;
    private String question;
    private String answer;
}
