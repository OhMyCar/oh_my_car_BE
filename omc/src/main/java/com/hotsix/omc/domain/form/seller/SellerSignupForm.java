package com.hotsix.omc.domain.form.seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


public class SellerSignupForm {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        @NotBlank(message = "이메일은 필수 입력값입니다.")
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
    public static class Response{
        private String email;
        private String name;

        public static Response from(Request request){
            return Response.builder()
                    .email(request.email)
                    .name(request.name)
                    .build();
        }

    }
}
