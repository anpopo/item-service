package hello.itemservice.web.login;

import hello.itemservice.domain.login.LoginService;
import hello.itemservice.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginForm") LoginForm form,
            BindingResult bindingResult,
            HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호를 확인하세요.");
            return "login/loginForm";
        }

        // 쿠키에 세션정보가 없을경우 세션 쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");

        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}