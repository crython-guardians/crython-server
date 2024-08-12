package crypton.CryptoGuardians.global.error.exception;

import crypton.CryptoGuardians.global.util.ResponseUtil;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }

    public ResponseUtil.ResponseDto<?> body() {
        return ResponseUtil.error(getMessage(), null);
    }

    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
