package crypton.CryptoGuardians.domain.document.service;

import crypton.CryptoGuardians.domain.document.dto.*;
import crypton.CryptoGuardians.domain.document.entity.Document;

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
    void fileShare(Long documentId, DocShareRequestDTO shareRequestDTO);
}