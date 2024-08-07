package crypton.CryptoGuardians.domain.document.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "document_view_tb")
public class DocumentView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User viewer;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    public static DocumentView createDocumentView(Document document, User viewer, Timestamp createdAt) {
        DocumentView documentView = new DocumentView();
        documentView.document = document;
        documentView.viewer = viewer;
        documentView.createdAt = createdAt;
        return documentView;
    }
}
