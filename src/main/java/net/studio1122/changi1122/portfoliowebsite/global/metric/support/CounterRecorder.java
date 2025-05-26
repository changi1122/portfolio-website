package net.studio1122.changi1122.portfoliowebsite.global.metric.support;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CounterRecorder {

    private final MeterRegistry registry;

    public CounterRecorder(MeterRegistry registry) {
        this.registry = registry;
    }

    public void increment(String name, String ...tags) {
        registry.counter(name, tags).increment();
    }
}
