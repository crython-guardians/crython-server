package crypton.CryptoGuardians.domain.document.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "document_key_tb")
public class DocumentKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authKey;

    public static DocumentKey createDocumentKey() {
        DocumentKey documentKey = new DocumentKey();
        documentKey.authKey = UUID.randomUUID().toString();
        return documentKey;
    }
}
