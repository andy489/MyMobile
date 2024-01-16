package com.soft.mobilele.web;

import com.soft.mobilele.model.dto.UserRegistrationDto;
import com.soft.mobilele.service.UserService;
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
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserRegistrationController extends GenericController {

    private static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";

    private final UserService userService;

    private final SecurityContextRepository securityContextRepository;

    private final LocaleResolver localeResolver;

    @Autowired
    public UserRegistrationController(UserService userService,
                                      SecurityContextRepository securityContextRepository,
                                      LocaleResolver localeResolver) {
        this.userService = userService;
        this.securityContextRepository = securityContextRepository;
        this.localeResolver = localeResolver;
    }

    @ModelAttribute("userRegistrationModel")
    public UserRegistrationDto initUserRegistrationModel() {

        return new UserRegistrationDto();
    }

    @GetMapping("/register")
    public ModelAndView register() {

        return super.view("auth/register");
    }

    @PostMapping("/register")
    public ModelAndView register(
            @Valid UserRegistrationDto userRegistrationDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("userRegistrationModel", userRegistrationDTO);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + "userRegistrationModel", bindingResult);

            return super.redirect("/users/register");
        }

        // https://stackoverflow.com/questions/75618616/auto-login-after-registration-spring-boot-3-spring-security-6
        userService.registerAndLogin(userRegistrationDTO, localeResolver.resolveLocale(request), successfulAuth -> {
            // populating security context
            SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();

            SecurityContext context = strategy.createEmptyContext();
            context.setAuthentication(successfulAuth);

            strategy.setContext(context);

            securityContextRepository.saveContext(context, request, response);
        });

        return super.redirect("/");
    }
}
