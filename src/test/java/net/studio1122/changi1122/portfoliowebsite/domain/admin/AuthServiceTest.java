package net.studio1122.changi1122.portfoliowebsite.domain.admin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    AuthService authService;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(Admin.class);
    }

    @DisplayName("최초로 관리자 계정을 생성합니다.")
    @Test
    void createAdmin() {
        // when
        authService.createAdmin("test", "test");

        // then
        assertThat(mongoTemplate.count(new Query(), Admin.class)).isEqualTo(1);
    }

    @DisplayName("최초 계정 생성 뒤 새로운 관리자 계정을 생성할 수 없습니다.")
    @Test
    void createAdminWhenExists() {
        // given
        authService.createAdmin("test", "test");

        // when then
        assertThatThrownBy(() -> authService.createAdmin("test2", "test2"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("관리자 계정에 로그인합니다.")
    @Test
    void login() {
        // given
        authService.createAdmin("test", "test");

        // when
        Admin admin = authService.login("test", "test");

        // then
        assertThat(admin).isNotNull();
    }

    @DisplayName("존재하지 않는 아이디로 로그인할 수 없습니다.")
    @Test
    void loginNoSuchLoginId() {
        // given
        authService.createAdmin("test", "test");

        // when then
        assertThatThrownBy(() -> authService.login("no-such-id", "test"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("비밀번호가 일치하지 않으면 로그인할 수 없습니다.")
    @Test
    void loginPasswordNotMatched() {
        // given
        authService.createAdmin("test", "test");

        // when then
        assertThatThrownBy(() -> authService.login("test", "password"))
                .isInstanceOf(IllegalArgumentException.class);
    }


}