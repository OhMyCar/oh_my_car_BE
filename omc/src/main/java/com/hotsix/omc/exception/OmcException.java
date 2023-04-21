package com.hotsix.omc.exception;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OmcException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;
    public OmcException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getErrorMessage();
    }
}
