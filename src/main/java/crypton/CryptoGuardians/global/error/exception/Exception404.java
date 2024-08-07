package crypton.CryptoGuardians.global.error.exception;

import crypton.CryptoGuardians.global.util.ResponseUtil;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 404 찾을 수 없음
@Getter
public class Exception404 extends RuntimeException {
    public Exception404(String message) {
        super(message);
    }

    public ResponseUtil.ResponseDto<?> body() {
        return ResponseUtil.failure(getMessage(), null);
    }

    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
