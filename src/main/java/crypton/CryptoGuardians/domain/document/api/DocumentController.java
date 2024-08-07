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


import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/document")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseDto<String> uploadFile(@ModelAttribute UploadRequestDTO uploadRequestDTO){
        try {
            documentService.saveFile(uploadRequestDTO);
            return ResponseUtil.success("File uploaded successfully", null);
        } catch (IOException e) {
            return ResponseUtil.error(e.getMessage(), null);
        }
    }

    @GetMapping("/{id}/authorize")
    public ResponseEntity<ResponseDto<AuthorizeResponseDTO>> authorize(
            @PathVariable("id") @NotNull @Min(1) Long documentId
    ){
        AuthorizeResponseDTO response = documentService.getAuthorizeKey(documentId);
        return new ResponseEntity<>(ResponseUtil.success("파일 열람 인증 성공", response), HttpStatus.OK);
    }
}