package crypton.CryptoGuardians.domain.document.dto;

import crypton.CryptoGuardians.domain.document.entity.Document;

import java.sql.Timestamp;

public record DocumentResponseDTO(
        String fileName,
        int fileReadCount,
        String fileSize,
        int fileTheftCount,
        boolean updateAuthKey,
        Timestamp createdAt,
        String uploadUser
) {
    public static DocumentResponseDTO from(Document document) {
        return new DocumentResponseDTO(
                document.getFileName().substring(document.getFileName().indexOf("_") + 1),
                document.getFileReadCount(),
                document.getFileSize(),
                document.getFileTheftCount(),
                document.isUpdateAuthKey(),
                document.getCreatedAt(),
                document.getUploadUser().getUsername()
        );
    }
}