package crypton.CryptoGuardians.domain.document.service;

import crypton.CryptoGuardians.domain.document.dto.DocumentResponseDTO;
import crypton.CryptoGuardians.domain.document.dto.DownloadResponseDTO;
import crypton.CryptoGuardians.domain.document.dto.UploadRequestDTO;
import crypton.CryptoGuardians.domain.document.dto.AuthorizeResponseDTO;
import crypton.CryptoGuardians.domain.document.entity.Document;
import crypton.CryptoGuardians.domain.document.dto.ViewLogRequestDTO;
import crypton.CryptoGuardians.domain.document.dto.ReportResponseDTO;

import java.util.List;

public interface DocumentService {

    void saveFile(UploadRequestDTO uploadRequestDTO);
    DownloadResponseDTO loadFileAsResource(Long documentId);
    void deleteFile(Long documentId);
    List<DocumentResponseDTO> getFilesByUser(Long userId);
    Document findById(Long documentId);
    AuthorizeResponseDTO getAuthorizeKey(Long documentId);
    ReportResponseDTO getReport(Long documentId);
    void saveViewLog(Long documentId, ViewLogRequestDTO viewLogRequestDTO);
}