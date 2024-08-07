package crypton.CryptoGuardians.global.error.exception;

import crypton.CryptoGuardians.global.util.ResponseUtil;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 401 인증 안됨
@Getter
public class Exception401 extends RuntimeException {
    public Exception401(String message) {
        super(message);
    }

    public ResponseUtil.ResponseDto<?> body() {
        return ResponseUtil.failure(getMessage(), null);
    }

    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}