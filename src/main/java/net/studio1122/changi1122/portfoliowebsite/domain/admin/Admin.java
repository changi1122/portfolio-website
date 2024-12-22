package net.studio1122.changi1122.portfoliowebsite.domain.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("admin")
public class Admin {

    String loginId;
    String password;

    @Builder
    public Admin(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
