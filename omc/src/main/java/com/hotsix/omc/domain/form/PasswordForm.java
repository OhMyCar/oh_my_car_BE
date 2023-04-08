package com.hotsix.omc.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class PasswordForm {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        @Email
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        private String email;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private String email;


    }
}
