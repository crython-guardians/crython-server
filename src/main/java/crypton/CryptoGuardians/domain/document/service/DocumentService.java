package crypton.CryptoGuardians.domain.document.service;

import crypton.CryptoGuardians.domain.document.dto.DocRequestDTO;

import java.io.IOException;

public interface DocumentService {

    void saveFile(DocRequestDTO docRequestDTO) throws IOException;

}