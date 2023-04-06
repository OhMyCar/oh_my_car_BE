package com.hotsix.omc.domain.form.customer;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerLoginForm {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
