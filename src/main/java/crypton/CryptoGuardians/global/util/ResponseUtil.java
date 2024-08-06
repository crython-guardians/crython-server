package crypton.CryptoGuardians.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ResponseUtil {

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(true, message, data);
    }

    public static <T> ResponseDto<T> failure(String message, T data) {
        return new ResponseDto<>(false, message, data);
    }

    public static <T> ResponseDto<T> error(String message, T data) {
        return new ResponseDto<>(false, message, data);
    }

    @Getter
    @AllArgsConstructor
    public static class ResponseDto<T> {
        private final boolean success;
        private final String message;
        private final T data;
    }
}
