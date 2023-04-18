package com.hotsix.omc.domain.form.seller;

import com.hotsix.omc.domain.entity.Category;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

public class StoreRegisterForm {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email
        private String email;
        @NotBlank(message = "가게 이름은 필수 입력 값입니다.")
        private String name;
        @NotBlank(message = "도시는 필수 입력 값입니다.")
        private String city;
        @NotBlank(message = "주소는 필수 입력 값입니다.")
        private String street;
        @NotBlank(message = "우편번호는 필수 입력 값입니다.")
        private String zipcode;
        @NotBlank(message = "전화번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다. xxx-xxxx-xxxx")
        private String tel;
        @NotBlank(message = "가게 여는 시간은 필수 입력 값입니다.")
        @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$", message = "올바른 시간 형식이 아닙니다")
        private String open;
        @NotBlank(message = "가게 닫는 시간은 필수 입력 값입니다.")
        @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$", message = "올바른 시간 형식이 아닙니다")
        private String close;

        private boolean category1;
        private boolean category2;
        private boolean category3;
        private boolean category4;
        private boolean category5;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String name;
        private String tel;
        private String address;
        private String open;
        private String close;
        private List<Category> categories;


        public static Response from(Request request) {
            return Response.builder()
                    .name(request.name)
                    .tel(request.tel)
                    .address(request.city + " " + request.street + " " + request.zipcode)
                    .open(request.open)
                    .close(request.close)
                    .categories(Category.of(request))
                    .build();
        }
    }


}
