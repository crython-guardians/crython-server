package crypton.CryptoGuardians.domain.document.api;

import crypton.CryptoGuardians.domain.document.dto.UploadRequestDTO;
import crypton.CryptoGuardians.domain.document.dto.AuthorizeResponseDTO;
import crypton.CryptoGuardians.domain.document.service.DocumentService;
import crypton.CryptoGuardians.global.util.ResponseUtil;
import crypton.CryptoGuardians.global.util.ResponseUtil.ResponseDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/document")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseDto<String>> uploadFile(@ModelAttribute UploadRequestDTO uploadRequestDTO){
            documentService.saveFile(uploadRequestDTO);
            return new ResponseEntity<>(ResponseUtil.success("파일 업로드 성공", null), HttpStatus.OK);
    }

    @GetMapping("/{id}/authorize")
    public ResponseEntity<ResponseDto<AuthorizeResponseDTO>> authorize(
            @PathVariable("id") @NotNull @Min(1) Long documentId
    ){
        AuthorizeResponseDTO response = documentService.getAuthorizeKey(documentId);
        return new ResponseEntity<>(ResponseUtil.success("파일 열람 인증 성공", response), HttpStatus.OK);
    }
}