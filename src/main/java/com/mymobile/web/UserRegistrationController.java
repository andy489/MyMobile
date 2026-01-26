package com.mymobile.web;

import com.mymobile.config.ReCaptchaConfig;
import com.mymobile.model.dto.ReCaptchaResponseDto;
import com.mymobile.model.dto.UserRegistrationDto;
import com.mymobile.service.UserService;
import com.mymobile.service.recapthca.ReCaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserRegistrationController extends GenericController {

    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";

    private final UserService userService;

    private final SecurityContextRepository securityContextRepository;

    private final LocaleResolver localeResolver;

    ReCaptchaConfig reCaptchaConfig;

    private final ReCaptchaService reCaptchaService;

    @Autowired
    public UserRegistrationController(UserService userService,
                                      SecurityContextRepository securityContextRepository,
                                      LocaleResolver localeResolver,
                                      ReCaptchaService reCaptchaService,
                                      ReCaptchaConfig reCaptchaConfig) {
        this.userService = userService;
        this.securityContextRepository = securityContextRepository;
        this.localeResolver = localeResolver;
        this.reCaptchaService = reCaptchaService;
        this.reCaptchaConfig = reCaptchaConfig;
    }

    @ModelAttribute("userRegistrationModel")
    public UserRegistrationDto initUserRegistrationModel() {

        return new UserRegistrationDto();
    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView modelAndView = super.view("auth/register");

        if (reCaptchaConfig != null && reCaptchaConfig.getSite() != null) {
            modelAndView.addObject("recaptchaSiteKey", reCaptchaConfig.getSite());

            System.out.println("DEBUG: Site key passed to view: " +
                    reCaptchaConfig.getSite().substring(reCaptchaConfig.getSite().length() - 5));
        } else {
            System.err.println("ERROR: reCaptchaConfig or site key is null!");
        }

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(
            @Valid UserRegistrationDto userRegistrationDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(name = "g-recaptcha-response") String reCaptchaResponse) {


        Optional<ReCaptchaResponseDto> reCaptchaValidation = reCaptchaService.verify(reCaptchaResponse);

        if (reCaptchaValidation.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Captcha service unavailable");
            return super.redirect("/users/register");
        }

        ReCaptchaResponseDto reCaptchaDto = reCaptchaValidation.get();

        // 2. Check if successful AND score is acceptable (e.g., > 0.5)
        boolean isBot = !reCaptchaDto.isSuccess() || reCaptchaDto.getScore() < 0.5;

        if (isBot) {
            redirectAttributes.addFlashAttribute("error",
                    "Suspicious activity detected. Score: " + reCaptchaDto.getScore());
            return super.redirect("/users/register");
        }

        // 3. OPTIONAL: Verify the action matches
        if (!"register".equals(reCaptchaDto.getAction())) {
            redirectAttributes.addFlashAttribute("error", "Invalid captcha action");
            return super.redirect("/users/register");
        }

        // 4. Continue with your existing validation and registration logic
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistrationModel", userRegistrationDTO);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + "userRegistrationModel", bindingResult);
            return super.redirect("/users/register");
        }

        // Rest of your registration logic...
        userService.registerAndLogin(userRegistrationDTO, localeResolver.resolveLocale(request), successfulAuth -> {
            SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
            SecurityContext context = strategy.createEmptyContext();
            context.setAuthentication(successfulAuth);
            strategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);
        });

        return super.redirect("registration-success");
    }

    @GetMapping("/registration-success")
    public ModelAndView registrationSuccess() {
        return super.view("auth/registration-success");
    }
}
