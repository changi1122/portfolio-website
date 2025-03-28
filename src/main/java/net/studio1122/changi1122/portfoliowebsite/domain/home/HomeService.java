package net.studio1122.changi1122.portfoliowebsite.domain.home;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.home.entity.Home;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeService {

    private final HomeRepository homeRepository;

    @Transactional
    public Home createHome(Home request) {
        if (homeRepository.existsByAccessKey(request.getAccessKey()))
            throw new IllegalArgumentException("accessKey가 동일한 홈화면 객체가 존재합니다.");

        return homeRepository.save(request);
    }

    @Transactional
    public Home updateHome(String accessKey, Home request) {
        Home home = homeRepository.findByAccessKey(accessKey).orElseThrow();

        home.setInterests(request.getInterests());
        home.setIntro(request.getIntro());
        home.setQuestions(request.getQuestions());
        home.setExpireDate(request.getExpireDate());
        home.setProjects(request.getProjects());
        return homeRepository.save(home);
    }

    public Home readHome(String accessKey) {
        return homeRepository.findByAccessKey(accessKey).orElseThrow();
    }

    public Home readHome(String accessKey, LocalDate today) {
        Home home = homeRepository.findByAccessKey(accessKey).orElseThrow();

        if (home.getExpireDate().isBefore(today))
            throw new IllegalArgumentException("유효 기한이 만료되었습니다.");

        return home;
    }

    @Transactional
    public void deleteHome(String accessKey) {
        if (!homeRepository.existsByAccessKey(accessKey)) {
            throw new NoSuchElementException();
        }
        homeRepository.deleteByAccessKey(accessKey);
    }

    public Page<Home> listHome(Pageable pageable) {
        return homeRepository.findAllBy(pageable);
    }

    public boolean existsAccessKey(String accessKey) {
        return homeRepository.existsByAccessKey(accessKey);
    }

}
