package net.studio1122.changi1122.portfoliowebsite.web.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.studio1122.changi1122.portfoliowebsite.domain.admin.Admin;
import net.studio1122.changi1122.portfoliowebsite.domain.admin.AuthService;
import net.studio1122.changi1122.portfoliowebsite.global.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;

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

        HttpSession session = request.getSession();
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

    public String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For"); // 프록시를 통과한 경우 실제 IP
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr(); // 기본적으로 RemoteAddr 사용
        }
        return clientIp;
    }

}
