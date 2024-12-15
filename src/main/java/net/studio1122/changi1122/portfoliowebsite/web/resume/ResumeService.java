package net.studio1122.changi1122.portfoliowebsite.web.resume;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.Resume;
import net.studio1122.changi1122.portfoliowebsite.domain.resume.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

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

    public Resume readResume(String accessKey, LocalDate today) {
        Resume resume = resumeRepository.findByAccessKey(accessKey).orElseThrow();

        if (resume.getExpireDate().isBefore(today))
            throw new IllegalArgumentException("유효 기한이 만료되었습니다.");

        return resume;
    }

    public void deleteHome(String accessKey) {
        if (!resumeRepository.existsByAccessKey(accessKey)) {
            throw new NoSuchElementException();
        }
        resumeRepository.deleteByAccessKey(accessKey);
    }
}
