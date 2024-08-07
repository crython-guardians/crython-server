package crypton.CryptoGuardians.domain.document.dto;

import org.springframework.web.multipart.MultipartFile;

public record UploadRequestDTO(MultipartFile file, String uploadUser) {
}
