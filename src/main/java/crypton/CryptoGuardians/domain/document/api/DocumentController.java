package crypton.CryptoGuardians.domain.document.api;


import crypton.CryptoGuardians.domain.document.dto.UploadRequestDTO;
import crypton.CryptoGuardians.domain.document.service.DocumentService;
import crypton.CryptoGuardians.global.util.ResponseUtil;
import crypton.CryptoGuardians.global.util.ResponseUtil.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
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
}