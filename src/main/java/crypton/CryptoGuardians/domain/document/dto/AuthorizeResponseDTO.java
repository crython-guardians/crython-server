package crypton.CryptoGuardians.domain.document.dto;

import lombok.Getter;

@Getter
public class AuthorizeResponseDTO {
    private final String key;

    public AuthorizeResponseDTO(String key) {
        this.key = key;
    }
}
