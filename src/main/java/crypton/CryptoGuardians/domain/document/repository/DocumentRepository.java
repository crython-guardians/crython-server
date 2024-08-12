package crypton.CryptoGuardians.domain.document.repository;


import crypton.CryptoGuardians.domain.document.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d left join DocumentShare ds on d.id = ds.fileId.id " +
            "WHERE d.uploadUser.id = :userId or ds.sharedUserId = :userId ORDER BY d.createdAt ASC")
    List<Document> findByUploadUserIdOrderByCreatedAtAsc(@Param("userId") Long userId);

    @Query("SELECT d FROM Document d " +
            "WHERE d.uploadUser.id = :userId AND d.fileName LIKE %:keyword% " +
            "ORDER BY d.createdAt ASC")
    List<Document> searchDocumentOrderByCreatedAtAsc(Long userId, String keyword);
}
