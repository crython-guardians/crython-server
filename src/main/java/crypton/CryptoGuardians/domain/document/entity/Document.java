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

    @Column
    private String uploadUser;

    @Column
    private String filePath;

    // 문서 탈취 여부
    @Column
    private boolean fileTheft;

    @Column
    private int fileReadCount;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    public Document(String fileName, String fileSize, String uploadUser, String filePath, boolean fileTheft){
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.uploadUser = uploadUser;
        this.filePath = filePath;
        this.fileTheft = fileTheft;
    }

    public void fileRead() {
        this.fileReadCount++;
    }
}
