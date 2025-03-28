package net.studio1122.changi1122.portfoliowebsite.domain.project;

import net.studio1122.changi1122.portfoliowebsite.domain.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends MongoRepository<Project, String> {

    Optional<Project> findByName(String name);

    Page<Project> findAllBy(Pageable pageable);

    List<Project> findByIsHiddenFalseOrderByOrderAsc();
}
