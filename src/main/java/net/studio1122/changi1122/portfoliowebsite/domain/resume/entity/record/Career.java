package net.studio1122.changi1122.portfoliowebsite.domain.resume.entity.record;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Career extends Record {

    String position;
    List<String> tags;
    String themeColor;

}
