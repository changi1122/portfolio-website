package net.studio1122.changi1122.portfoliowebsite.domain.resume.record;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Record {

    String duration;
    String name;
    String description;
    List<String> links;

}
