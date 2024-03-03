package com.soft.mobilele.model.dto;

import com.soft.mobilele.model.validation.register.UniqueUsername;
import com.soft.mobilele.model.validation.register.FieldMatch;
import com.soft.mobilele.model.validation.register.UniqueUserEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@FieldMatch(
        firstField = "password",
        secondField = "confirmPassword",
        message = "passwords do not match"
)
public class UserRegistrationDto {

    @NotBlank(message = "username should be provided and not blank")
    @Size(min = 2, max = 20, message = "provided username must be between 2 and 20 characters long")
    @UniqueUsername(message = "username should be unique")
    private String username;

    @NotBlank(message = "user email should be provided and not blank")
    @Email(message = "user email should be valid")
    @UniqueUserEmail(message = "user email should be unique")
    private String email;

    @NotBlank(message = "first name must be provided!")
    @Size(min = 2, max = 20, message = "provided first name must be between 2 and 20 characters long")
    private String firstName;

    @NotBlank(message = "last name must be provided")
    @Size(min = 2, max = 20, message = "The last first name must be between 2 and 20 characters long")
    private String lastName;

    @NotBlank(message = "password must be provided")
    @Size(min = 4, max = 20, message = "provided password must be between 4 and 20 characters long")
    private String password;

    private String confirmPassword;

    public String getUserFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "UserRegisterDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + (password != null ? "[PROVIDED]" : null) + '\'' +
                ", confirmPassword='" + (confirmPassword != null ? "[PROVIDED]" : null) + '\'' +
                '}';
    }
}
