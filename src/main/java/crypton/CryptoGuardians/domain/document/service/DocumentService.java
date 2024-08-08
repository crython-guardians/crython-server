package crypton.CryptoGuardians.domain.document.service;

import crypton.CryptoGuardians.domain.document.dto.DownloadResponseDTO;
import crypton.CryptoGuardians.domain.document.dto.UploadRequestDTO;
import crypton.CryptoGuardians.domain.document.dto.AuthorizeResponseDTO;
import crypton.CryptoGuardians.domain.document.entity.Document;
import org.springframework.core.io.Resource;

import java.io.IOException;

public interface DocumentService {
    void saveFile(UploadRequestDTO uploadRequestDTO);
    AuthorizeResponseDTO getAuthorizeKey(Long documentId);
    DownloadResponseDTO loadFileAsResource(Long documentId);
}