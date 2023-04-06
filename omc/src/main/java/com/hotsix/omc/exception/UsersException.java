package com.hotsix.omc.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;
    public UsersException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getErrorMessage();
    }
}
