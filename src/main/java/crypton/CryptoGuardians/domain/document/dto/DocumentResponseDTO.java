package crypton.CryptoGuardians.domain.document.dto;

import java.sql.Timestamp;

public record DocumentResponseDTO(String fileName, int fileReadCount, String fileSize,
                                  int fileTheftCount, boolean updateAuthKey, Timestamp createdAt, String uploadUser) {
}