package net.studio1122.changi1122.portfoliowebsite.domain.resume;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResumeRepository extends MongoRepository<Resume, String> {

    Optional<Resume> findByAccessKey(String accessKey);

    void deleteByAccessKey(String accessKey);

    boolean existsByAccessKey(String accessKey);

}
