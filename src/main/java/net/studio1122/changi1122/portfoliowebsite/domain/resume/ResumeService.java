package net.studio1122.changi1122.portfoliowebsite.domain.resume;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.entity.Resume;
import net.studio1122.changi1122.portfoliowebsite.global.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    @Transactional
    public Resume createResume(Resume resume) {
        if (resumeRepository.existsByAccessKey(resume.getAccessKey()))
            throw new IllegalArgumentException("accessKey가 동일한 이력서 객체가 존재합니다.");

        return resumeRepository.save(resume);
    }

    @Transactional
    public Resume updateResume(String accessKey, Resume request) {
        Resume resume = resumeRepository.findByAccessKey(accessKey).orElseThrow();

        resume.setExpireDate(request.getExpireDate());
        resume.setLinks(request.getLinks());
        resume.setIntro(request.getIntro());
        resume.setInterests(request.getInterests());
        resume.setQuestions(request.getQuestions());
        resume.setExperiences(request.getExperiences());
        resume.setDegrees(request.getDegrees());
        resume.setProjects(request.getProjects());
        resume.setCertifications(request.getCertifications());
        resume.setAwards(request.getAwards());
        resume.setCourseCertifications(request.getCourseCertifications());

        return resumeRepository.save(resume);
    }

    public Resume readResume(String accessKey) {
        return resumeRepository.findByAccessKey(accessKey).orElseThrow();
    }

    public Resume readResume(String accessKey, LocalDate today) {
        Resume resume = resumeRepository.findByAccessKey(accessKey).orElseThrow();

        if (resume.isExpired(today))
            throw new IllegalArgumentException("유효 기한이 만료되었습니다.");

        return resume;
    }

    public Resume readResumeOrDefault(String accessKey, LocalDate today) {
        return Optional.ofNullable(accessKey)
                .filter(StringUtils::hasText)
                .flatMap(resumeRepository::findByAccessKey)
                .filter(resume -> !resume.isExpired(today))
                .orElseGet(() -> readResume(AppConstants.DEFAULT_ACCESS_KEY));
    }

    public void deleteResume(String accessKey) {
        if (!resumeRepository.existsByAccessKey(accessKey)) {
            throw new NoSuchElementException();
        }
        resumeRepository.deleteByAccessKey(accessKey);
    }

    public Page<Resume> listResume(Pageable pageable) {
        return resumeRepository.findAllBy(pageable);
    }

    public boolean existsAccessKey(String accessKey) {
        return resumeRepository.existsByAccessKey(accessKey);
    }
}
