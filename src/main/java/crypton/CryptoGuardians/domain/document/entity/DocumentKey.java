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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    private String authKey;

    public static DocumentKey createDocumentKey(Document document){
        DocumentKey documentKey = new DocumentKey();
        documentKey.document = document;
        documentKey.authKey = UUID.randomUUID().toString();
        return documentKey;
    }
}
