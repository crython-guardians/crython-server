package crypton.CryptoGuardians.domain.document.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record ViewLogRequestDTO(
        @NotNull @Min(1) Long viewerId,
        Timestamp timestamp
) {

}
