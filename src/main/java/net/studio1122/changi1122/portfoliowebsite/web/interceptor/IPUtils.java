package net.studio1122.changi1122.portfoliowebsite.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;

public class IPUtils {

    public static String getClientIp(HttpServletRequest request) {

        String clientIp = null;

        // 프록시 통과시 클라이언트와 중간 프록시 서버 IP 체인
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank())
            clientIp = "XFF=" + request.getHeader("X-Forwarded-For");

        if (clientIp == null) {
            // 프록시 통과시 클라이언트의 IP 주소
            String xRealIP = request.getHeader("X-Real-IP");

            if (xRealIP != null && !xRealIP.isBlank())
                clientIp = "XRI=" + request.getHeader("X-Real-IP");
        }

        if (clientIp == null) {
            // TCP/IP 연결 IP 주소 (직접 접속시 클라이언트, 프록시 통과시 프록시 주소)
            clientIp = "ADDR=" + request.getRemoteAddr();
        }
        return clientIp;
    }
}
