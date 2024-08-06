package crypton.CryptoGuardians.domain.document.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor
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
    private String uploadDay;

    public Document(String fileName, String fileSize, String uploadUser, String filePath, boolean fileTheft){
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.uploadUser = uploadUser;
        this.filePath = filePath;
        this.fileTheft = fileTheft;
        this.uploadDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
