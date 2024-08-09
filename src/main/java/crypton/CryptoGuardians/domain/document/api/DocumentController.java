package crypton.CryptoGuardians.domain.document.api;

import crypton.CryptoGuardians.domain.document.dto.DocumentResponseDTO;
import crypton.CryptoGuardians.domain.document.dto.DownloadResponseDTO;
import crypton.CryptoGuardians.domain.document.dto.ReportResponseDTO;
import crypton.CryptoGuardians.domain.document.dto.ViewLogRequestDTO;
import crypton.CryptoGuardians.domain.document.dto.UploadRequestDTO;
import crypton.CryptoGuardians.domain.document.dto.AuthorizeResponseDTO;
import crypton.CryptoGuardians.domain.document.service.DocumentService;
import crypton.CryptoGuardians.global.util.ResponseUtil;
import crypton.CryptoGuardians.global.util.ResponseUtil.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/document")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<List<DocumentResponseDTO>>> getFilesByUser(
            @PathVariable("id") @NotNull @Min(1) Long userId
    ) {
        List<DocumentResponseDTO> documents = documentService.getFilesByUser(userId);
        return new ResponseEntity<>(ResponseUtil.success("파일 리스트 조회 성공", documents), HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseDto<String>> uploadFile(@ModelAttribute UploadRequestDTO uploadRequestDTO){
            documentService.saveFile(uploadRequestDTO);
            return new ResponseEntity<>(ResponseUtil.success("파일 업로드 성공", null), HttpStatus.OK);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("id") @NotNull @Min(1) Long documentId
    ) {
        DownloadResponseDTO downloadResponseDTO = documentService.loadFileAsResource(documentId);
        Resource resource = downloadResponseDTO.resource();
        String fileName = downloadResponseDTO.fileName();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ResponseDto<String>> deleteFile(
            @PathVariable("id") @NotNull @Min(1) Long documentId
    ) {
        documentService.deleteFile(documentId);
        return new ResponseEntity<>(ResponseUtil.success("파일 삭제 성공", null), HttpStatus.OK);
    }

    @GetMapping("/{id}/authorize")
    public ResponseEntity<ResponseDto<AuthorizeResponseDTO>> authorize(
            @PathVariable("id") @NotNull @Min(1) Long documentId
    ){
        AuthorizeResponseDTO response = documentService.getAuthorizeKey(documentId);
        return new ResponseEntity<>(ResponseUtil.success("파일 열람 인증 성공", response), HttpStatus.OK);
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<ResponseDto<ReportResponseDTO>> report(
            @PathVariable("id") @NotNull @Min(1) Long documentId
    ){
        ReportResponseDTO reportResponseDTO = documentService.getReport(documentId);
        return new ResponseEntity<>(ResponseUtil.success("파일 리포트 조회 성공", reportResponseDTO), HttpStatus.OK);
    }

    @PostMapping("/{id}/view-log")
    public ResponseEntity<ResponseDto<String>> viewLog(
            @PathVariable("id") @NotNull @Min(1) Long documentId,
            @RequestBody @Valid ViewLogRequestDTO viewLogRequestDTO
    ){
        documentService.saveViewLog(documentId, viewLogRequestDTO);
        return new ResponseEntity<>(ResponseUtil.success("파일 열람 로그 저장 성공", null), HttpStatus.OK);
    }
}
