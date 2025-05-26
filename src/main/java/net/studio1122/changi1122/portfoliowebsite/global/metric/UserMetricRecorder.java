package net.studio1122.changi1122.portfoliowebsite.global.metric;

import lombok.RequiredArgsConstructor;
import net.studio1122.changi1122.portfoliowebsite.global.metric.model.MetricNames;
import net.studio1122.changi1122.portfoliowebsite.global.metric.model.MetricTags;
import net.studio1122.changi1122.portfoliowebsite.global.metric.support.CounterRecorder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMetricRecorder {

    private final CounterRecorder counter;

    /**
     * 페이지 조회 수를 카운트합니다.
     * @param path URL 경로 (ex. "/project/1")
     */
    public void countPageView(String path) {
        counter.increment(MetricNames.PAGE_VIEW_COUNTER, MetricTags.PATH, path);
    }
}
