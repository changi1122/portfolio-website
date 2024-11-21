package net.studio1122.changi1122.portfoliowebsite.domain.home;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HomeRepository extends MongoRepository<Home, String> {

    Home findByAccessKey(String accessKey);

}
