package net.studio1122.changi1122.portfoliowebsite.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import net.studio1122.changi1122.portfoliowebsite.global.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class GetOnlyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 미인증자는 GET 요청만 허용
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_ID) == null) {
            log.info("미인증 GET 이외 요청 시도 : [{}] from [{}]", requestURI, getClientIp(request));
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }

    public String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For"); // 프록시를 통과한 경우 실제 IP
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr(); // 기본적으로 RemoteAddr 사용
        }
        return clientIp;
    }
}
