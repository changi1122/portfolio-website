package net.studio1122.changi1122.portfoliowebsite.domain.home;

import net.studio1122.changi1122.portfoliowebsite.domain.home.entity.Home;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface HomeRepository extends MongoRepository<Home, String> {

    Optional<Home> findByAccessKeyAndExpireDateGreaterThanEqual(String accessKey, LocalDate today);

    Optional<Home> findByAccessKey(String accessKey);

    void deleteByAccessKey(String accessKey);

    boolean existsByAccessKey(String accessKey);

    Page<Home> findAllBy(Pageable pageable);
}
