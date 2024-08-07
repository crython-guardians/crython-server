package crypton.CryptoGuardians.domain.document.service;

import crypton.CryptoGuardians.domain.document.dto.UploadRequestDTO;

import java.io.IOException;

public interface DocumentService {

    void saveFile(UploadRequestDTO uploadRequestDTO) throws IOException;

}