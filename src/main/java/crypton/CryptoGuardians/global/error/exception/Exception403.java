package crypton.CryptoGuardians.global.error.exception;

import crypton.CryptoGuardians.global.util.ResponseUtil;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 403 권한 없음
@Getter
public class Exception403 extends RuntimeException {
    public Exception403(String message) {
        super(message);
    }

    public ResponseUtil.ResponseDto<?> body() {
        return ResponseUtil.failure(getMessage(), null);
    }

    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }
}