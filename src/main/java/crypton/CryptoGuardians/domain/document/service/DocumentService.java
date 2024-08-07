package crypton.CryptoGuardians.domain.document.service;

import crypton.CryptoGuardians.domain.document.dto.DownloadResponseDTO;
import crypton.CryptoGuardians.domain.document.dto.UploadRequestDTO;
import crypton.CryptoGuardians.domain.document.dto.AuthorizeResponseDTO;
import crypton.CryptoGuardians.domain.document.entity.Document;
import crypton.CryptoGuardians.domain.document.dto.ViewLogRequestDTO;

public interface DocumentService {

    void saveFile(UploadRequestDTO uploadRequestDTO);
    DownloadResponseDTO loadFileAsResource(Long documentId);
    void deleteFile(Long documentId);
    Document findById(Long documentId);
    AuthorizeResponseDTO getAuthorizeKey(Long documentId);
    void saveViewLog(Long documentId, ViewLogRequestDTO viewLogRequestDTO);
}