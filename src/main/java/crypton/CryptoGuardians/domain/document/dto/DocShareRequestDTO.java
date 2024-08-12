package crypton.CryptoGuardians.domain.document.dto;

import jakarta.validation.constraints.NotNull;

public record DocShareRequestDTO(@NotNull String sharedUser) {
}
