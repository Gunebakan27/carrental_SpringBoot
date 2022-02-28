package com.lecture.carrental.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lecture.carrental.domain.Role;
import com.lecture.carrental.domain.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {

    @Size(max = 15)
    @NotNull(message = "Please enter your first name")
    private String firstName;

    @Size(max = 15)
    @NotNull(message = "Please enter your last name")
    private String lastName;

    @JsonIgnore
    private String password;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please enter a valid phone number")
    @Size(min = 14, max = 14, message = "It must be 14 digit")
    @NotNull(message = "Please enter your phonenumber")
    private String phoneNumber;

    @Email(message = "Please enter a valid email")
    @Size(min = 5, max = 150)
    @NotNull(message = "Please enter your email")
    private String email;

    @Size(max = 250)
    @NotNull(message = "Please enter your address")
    private String address;

    @Size(max = 15)
    @NotNull(message = "Please enter your zip code")
    private String zipCode;

    private Boolean builtIn;

    private Set<String> roles;

    public void setRoles(Set<Role> roles) {
        Set<String> roles1 = new HashSet<>();
        Role[] role = roles.toArray(new Role[roles.size()]);
        for (int i = 0; i < roles.size(); i++) {
            if (role[i].getName().equals(UserRole.ROLE_ADMIN))
                roles1.add("Administrator");
            else
                roles1.add("Customer");
        }
        this.roles = roles1;
    }

    public UserDTO(String firstName, String lastName, String phoneNumber, String email, String address, String zipCode, Boolean builtIn, Set<String> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.zipCode = zipCode;
        this.builtIn = builtIn;
        this.roles = roles;
    }
}
