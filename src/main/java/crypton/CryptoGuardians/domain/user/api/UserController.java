package crypton.CryptoGuardians.domain.user.api;


import crypton.CryptoGuardians.domain.user.dto.UserRequestDto;
import crypton.CryptoGuardians.domain.user.entity.User;
import crypton.CryptoGuardians.domain.user.service.UserService;
import crypton.CryptoGuardians.global.util.ResponseUtil;
import crypton.CryptoGuardians.global.util.ResponseUtil.ResponseDto;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<String>> register(@RequestBody UserRequestDto userRequestDto) {
        try {
            User user = userService.register(userRequestDto.username(), userRequestDto.password());
            return new ResponseEntity<>(ResponseUtil.success("회원가입 성공", user.getUsername()), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseUtil.failure("회원가입 실패", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Long>> login(@RequestBody UserRequestDto userRequestDto) {
        try {
            User user = userService.login(userRequestDto.username(), userRequestDto.password());
            return new ResponseEntity<>(ResponseUtil.success("로그인 성공", user.getId()), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseUtil.failure("로그인 실패: " + e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<ResponseDto<Boolean>> checkUsername(@RequestParam String username) {
        boolean isDuplicate = userService.checkDuplicate(username);
        return new ResponseEntity<>(ResponseUtil.success("중복 아이디 결과", isDuplicate), HttpStatus.OK);
    }

}
