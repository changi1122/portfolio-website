package net.studio1122.changi1122.portfoliowebsite.domain.admin;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MongoTemplate mongoTemplate;

    public Admin createAdmin(String loginId, String password) {
        if (mongoTemplate.count(new Query(), Admin.class) > 0) {
            throw new IllegalArgumentException("한 개의 계정만 생성될 수 있습니다.");
        }

        Admin admin = Admin.builder()
                .loginId(loginId)
                .password(BCrypt.hashpw(password, BCrypt.gensalt(12)))
                .build();

        admin = mongoTemplate.insert(admin);
        return admin;
    }

    public Admin login(String loginId, String password) {
        Query query = new Query();
        query.addCriteria(Criteria.where("loginId").is(loginId));

        Admin admin = mongoTemplate.findOne(query, Admin.class);
        if (admin == null) {
            throw new NoSuchElementException("아이디가 일치하는 계정이 없습니다.");
        }

        if (!BCrypt.checkpw(password, admin.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return admin;
    }

    public boolean existsAdmin() {
        return (mongoTemplate.count(new Query(), Admin.class) > 0);
    }

}
