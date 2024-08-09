package crypton.CryptoGuardians.domain.document.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fileName;

    @Column
    private String fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User uploadUser;

    @Column
    private String filePath;

    // 문서 탈취 여부
    @Column
    private int fileTheftCount;

    @Column
    // 키가 바뀌었는지 여부
    private boolean updateAuthKey;

    @Column
    private int fileReadCount;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_key_id", referencedColumnName = "id")
    private DocumentKey documentKey;

    public Document(String fileName, String fileSize, User uploadUser, String filePath, boolean updateAuthKey) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.uploadUser = uploadUser;
        this.filePath = filePath;
        this.updateAuthKey = updateAuthKey;
    }

    public void fileRead() {
        this.fileReadCount++;
    }

    public void fileTheft() {
        this.fileTheftCount++;
    }

    public void setDocumentKey(DocumentKey documentKey) {
        this.documentKey = documentKey;
    }
}
