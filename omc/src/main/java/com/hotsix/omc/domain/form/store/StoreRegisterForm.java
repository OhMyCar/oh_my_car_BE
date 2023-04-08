package com.hotsix.omc.domain.form.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreRegisterForm {

  @NotBlank(message = "셀러 이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "가게 여는 시간은 필수 입력 값입니다.")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$", message = "올바른 시간 형식이 아닙니다")
    private String open;
    @NotBlank(message = "가게 닫는 시간은 필수 입력 값입니다.")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$", message = "올바른 시간 형식이 아닙니다")
    private String close;

    @NotBlank(message = "가게 이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다. xxx-xxxx-xxxx")
    private String tel;

    @NotBlank(message = "도시는 필수 입력 값입니다.")
    private String city;
    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String street;
    @NotBlank(message = "우편번호는 필수 입력 값입니다.")
    private String zipcode;
    private boolean category1;
    private boolean category2;
    private boolean category3;
    private boolean category4;
    private boolean category5;




}
