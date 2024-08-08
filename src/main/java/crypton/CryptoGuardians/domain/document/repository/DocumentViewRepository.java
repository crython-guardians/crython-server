package crypton.CryptoGuardians.domain.document.repository;

import crypton.CryptoGuardians.domain.document.entity.DocumentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentViewRepository extends JpaRepository<DocumentView, Long> {
    List<DocumentView> findAllByDocumentId(Long documentId);
}
