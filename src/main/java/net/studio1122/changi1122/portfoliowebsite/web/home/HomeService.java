package net.studio1122.changi1122.portfoliowebsite.web.home;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.domain.home.Home;
import net.studio1122.changi1122.portfoliowebsite.domain.home.HomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class HomeService {

    private final HomeRepository homeRepository;

    public Home createHome(Home request) {
        if (homeRepository.existsByAccessKey(request.getAccessKey()))
            throw new IllegalArgumentException("accessKey가 동일한 홈화면 객체가 존재합니다.");

        return homeRepository.save(request);
    }

    public Home updateHome(String accessKey, Home request) {
        Home home = homeRepository.findByAccessKey(accessKey).orElseThrow();

        home.setInterests(request.getInterests());
        home.setIntro(request.getIntro());
        home.setQuestions(request.getQuestions());
        home.setExpireDate(request.getExpireDate());
        return homeRepository.save(home);
    }

}
