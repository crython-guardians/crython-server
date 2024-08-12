package crypton.CryptoGuardians.domain.document.dto;

import org.springframework.core.io.Resource;

public record DownloadResponseDTO (String fileName, Resource resource) {
}
