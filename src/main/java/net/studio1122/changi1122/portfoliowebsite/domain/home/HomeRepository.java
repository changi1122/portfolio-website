package net.studio1122.changi1122.portfoliowebsite.domain.home;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface HomeRepository extends MongoRepository<Home, String> {

    Optional<Home> findByAccessKeyAndExpireDateGreaterThanEqual(String accessKey, LocalDate today);

    Optional<Home> findByAccessKey(String accessKey);

    boolean existsByAccessKey(String accessKey);

}
