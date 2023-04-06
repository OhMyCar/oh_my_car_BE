package com.hotsix.omc.domain.form.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


public class CustomerSignupForm {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email
        private String email;
        @NotBlank(message = "이름을 입력해주세요.")
        private String name;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
        @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
        private String phone;
        @NotBlank(message = "닉네임을 입력해주세요.")
        private String nickname;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Email
        private String email;
        private String name;

        public static Response from(Request request){
            return Response.builder()
                    .email(request.email)
                    .email(request.name)
                    .build();
        }
    }

}
