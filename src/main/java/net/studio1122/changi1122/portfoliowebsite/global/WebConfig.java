package net.studio1122.changi1122.portfoliowebsite.global;

import net.studio1122.changi1122.portfoliowebsite.web.interceptor.AuthInterceptor;
import net.studio1122.changi1122.portfoliowebsite.web.interceptor.GetOnlyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**", "/img/**", "/js/**", "/", "/error", "/login", "/logout",
                        "/signup", "/about", "/project", "/project/*", "/blog/*"
                );
        registry.addInterceptor(new GetOnlyInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/logout", "/signup");
    }
}
