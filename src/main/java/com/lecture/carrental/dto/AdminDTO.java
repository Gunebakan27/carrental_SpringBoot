package com.lecture.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {

    @Size(max=15)
    @NotNull(message = "Please enter your first name")
    private String firstName;

    @Size(max=15)
    @NotNull(message = "Please enter your last name")
    private String lastName;

    @Size(min=4,max=60)
    private String password;

    @Pattern(regexp="^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please enter a valid phone number")
    @Size(min=14,max=14,message = "It must be 14 digit")
    @NotNull(message = "Please enter your phonenumber")
    private String phoneNumber;

    @Email(message = "Please enter a valid email")
    @Size(min=5,max=150)
    @NotNull(message = "Please enter your email")
    private String email;

    @Size(max=250)
    @NotNull(message = "Please enter your address")
    private String address;

    @Size(max=15)
    @NotNull(message = "Please enter your zip code")
    private String zipCode;

    private Boolean builtIn;

    private Set<String> roles;

}
