package crypton.CryptoGuardians.domain.document.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "document_share_tb")
public class DocumentShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sharedUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id",
            foreignKey = @ForeignKey(
                    name = "fk_document_share_document_id",
                    foreignKeyDefinition = "FOREIGN KEY (document_id) REFERENCES document(id) ON DELETE CASCADE"
            )
    )
    private Document fileId;

    public DocumentShare(Long sharedUserId, Document document) {
        this.sharedUserId = sharedUserId;
        this.fileId = document;
    }
}
