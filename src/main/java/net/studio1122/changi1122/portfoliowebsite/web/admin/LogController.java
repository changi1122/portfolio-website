package net.studio1122.changi1122.portfoliowebsite.web.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Controller
public class LogController {

    @Value("${spring.profiles.active}")
    private String profileType;

    @Value(("${container.name}"))
    private String containerName;

    @GetMapping("/docker-logs")
    public ResponseEntity<String> getDockerLogs() {
        if (!"prod".equals(profileType)) {
            return ResponseEntity.ok("컨테이너 배포 환경에서만 로그가 출력됩니다.");
        }

        try {
            ProcessBuilder builder = new ProcessBuilder("sh", "-c", "docker logs " + containerName);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder logs = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                logs.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return ResponseEntity.ok(logs.toString());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("실행 중 오류 발생 (Exit code: " + exitCode + ")");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류: " + e.getMessage());
        }
    }
}
