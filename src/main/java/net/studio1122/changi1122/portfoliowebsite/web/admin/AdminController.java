package net.studio1122.changi1122.portfoliowebsite.web.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.studio1122.changi1122.portfoliowebsite.domain.admin.Admin;
import net.studio1122.changi1122.portfoliowebsite.domain.admin.AuthService;
import net.studio1122.changi1122.portfoliowebsite.domain.common.FileStore;
import net.studio1122.changi1122.portfoliowebsite.global.SessionConst;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.web.util.UriUtils;
import org.thymeleaf.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final FileStore fileStore;

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/manage") String redirectURL, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "admin/login";
        }

        try {
            Admin admin = authService.login(loginForm.getLoginId(), loginForm.getPassword());

            // 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_ID, admin.getLoginId());

            return "redirect:" + redirectURL;
        }
        catch (NoSuchElementException | IllegalArgumentException e) {
            log.info("[{}] : [{}]/[{}] from [{}]", e.getMessage(), loginForm.getLoginId(), loginForm.getPassword(),
                    getClientIp(request));

            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "admin/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult,
                         HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "admin/signup";
        }

        Admin admin = authService.createAdmin(loginForm.getLoginId(), loginForm.getPassword());

        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_ID, admin.getLoginId());

        return "redirect:/manage";
    }

    @PostMapping("/file")
    public String uploadFile(MultipartFile uploadFile,
                             @RequestParam(required = false) String storeName) throws IOException {

        if (!uploadFile.isEmpty()) {
            fileStore.storeFile(uploadFile, storeName, false);
        }
        return "redirect:/manage/file/list";
    }

    @DeleteMapping("/file/{filename}")
    public String deleteFile(@PathVariable String filename) throws IOException {
        if (!StringUtils.isEmpty(filename)) {
            fileStore.deleteFile(filename, false);
        }
        return "redirect:/manage/file/list";
    }

    @PostMapping("/image")
    public String uploadImage(MultipartFile uploadFile,
                             @RequestParam(required = false) String storeName) throws IOException {

        if (!uploadFile.isEmpty()) {
            fileStore.storeFile(uploadFile, storeName, true);
        }
        return "redirect:/manage/image/list";
    }

    @DeleteMapping("/image/{filename}")
    public String deleteImage(@PathVariable String filename) throws IOException {
        if (!StringUtils.isEmpty(filename)) {
            fileStore.deleteFile(filename, true);
        }
        return "redirect:/manage/image/list";
    }

    @GetMapping("/file/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(filename, false));
        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException(filename);
        }

        String encodedFilename = UriUtils.encode(filename, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) throws IOException {
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(filename, true));
        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException(filename);
        }

        MediaType contentType = MediaType.parseMediaType(Files.probeContentType(Path.of(filename)));

        return ResponseEntity.ok()
                .contentType(contentType)
                .body(resource);
    }


    /* View Rendering Method  */

    @GetMapping("/manage")
    public String adminHome(Model model) {

        model.addAttribute("content", "admin/fragment/home");

        return "admin/manage";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(defaultValue = "/manage") String redirectURL,
                            HttpServletRequest request, Model model) {
        if (!authService.existsAdmin())
            return "redirect:/signup";

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(SessionConst.LOGIN_ID) != null) {
            return "redirect:/manage";
        }

        model.addAttribute("redirectURL", redirectURL);
        model.addAttribute("loginForm", new LoginForm());

        return "admin/login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        if (authService.existsAdmin())
            return "redirect:/login";

        model.addAttribute("loginForm", new LoginForm());

        return "admin/signup";
    }

    @GetMapping("/manage/file/list")
    public String fileList(Model model) throws IOException {
        model.addAttribute("content", "admin/fragment/fileList");
        model.addAttribute("fileList", fileStore.listFiles(false));

        return "admin/manage";
    }

    @GetMapping("/manage/image/list")
    public String imageList(Model model) throws IOException {
        model.addAttribute("content", "admin/fragment/imageFileList");
        model.addAttribute("fileList", fileStore.listFiles(true));

        return "admin/manage";
    }

    public String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For"); // 프록시를 통과한 경우 실제 IP
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr(); // 기본적으로 RemoteAddr 사용
        }
        return clientIp;
    }

}
