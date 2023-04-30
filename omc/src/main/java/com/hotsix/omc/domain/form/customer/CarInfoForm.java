package com.hotsix.omc.domain.form.customer;


import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


public class CarInfoForm {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        private String nickname;
        private Long distance;
        private LocalDateTime lastModifiedAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private LocalDateTime registeredAt;
        private String nickname;
        public static Response from (Request request){
            return Response.builder()
                    .registeredAt(LocalDateTime.now())
                    .nickname(request.getNickname())
                    .build();
        }
    }

}
