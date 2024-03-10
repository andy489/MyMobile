package com.mobilele.model.validation.login;

import com.mobilele.repository.UserRepository;
import com.mobilele.model.entity.UserEntity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

public class UsernameAndPasswordMatchValidator implements ConstraintValidator<UsernameAndPasswordMatch, Object> {

    private String usernameField;

    private String passwordField;

    private String message;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public UsernameAndPasswordMatchValidator(
            UserRepository userRepository,
            PasswordEncoder encoder
    ) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public void initialize(UsernameAndPasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        this.usernameField = constraintAnnotation.usernameField();
        this.passwordField = constraintAnnotation.passwordField();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);

        String usernameFieldValue = Objects.requireNonNull(beanWrapper.getPropertyValue(this.usernameField)).toString();
        String passwordFieldValue = Objects.requireNonNull(beanWrapper.getPropertyValue(this.passwordField)).toString();

        boolean valid = true;

        Optional<UserEntity> byEmail = userRepository.findByUsername(usernameFieldValue);

        if (byEmail.isEmpty()) {
            valid = false;
        }

        if (valid) {
            String encodedPassword = byEmail.get().getPassword();
            valid = encoder.matches(passwordFieldValue, encodedPassword);
        }

        if (!valid) {
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(passwordField)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}
