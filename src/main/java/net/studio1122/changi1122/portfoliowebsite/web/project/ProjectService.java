package net.studio1122.changi1122.portfoliowebsite.web.project;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.project.Project;
import net.studio1122.changi1122.portfoliowebsite.domain.project.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public Project createProject(Project request) {
        return projectRepository.save(request);
    }

    @Transactional
    public Project updateProject(String id, Project request) {
        Project project = projectRepository.findById(id).orElseThrow();

        project.setName(request.getName());
        project.setImageSrc(request.getImageSrc());
        project.setDescription(request.getDescription());
        project.setKeywords(request.getKeywords());
        project.setLinks(request.getLinks());

        project.setCategory(request.getCategory());
        project.setBadges(request.getBadges());
        project.setBody(request.getBody());
        project.setBodyHtml(request.getBodyHtml());
        project.setBodyLinks(request.getBodyLinks());
        project.setOrder(request.getOrder());
        project.setThemeColor(request.getThemeColor());

        return projectRepository.save(project);
    }

    @Transactional
    public void toggleProjectVisibility(String id) {
        Project project = projectRepository.findById(id).orElseThrow();
        project.setHidden(!project.isHidden());
        projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(String id) {
        Project project = projectRepository.findById(id).orElseThrow();
        projectRepository.delete(project);
    }

    public Project readProject(String id) {
        return projectRepository.findById(id).orElseThrow();
    }

    public Page<Project> listProject(Pageable pageable) {
        return projectRepository.findAllBy(pageable);
    }

    public List<Project> listProjectVisible() {
        return projectRepository.findByIsHiddenFalseOrderByOrderAsc();
    }
}
