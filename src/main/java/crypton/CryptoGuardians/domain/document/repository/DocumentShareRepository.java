package crypton.CryptoGuardians.domain.document.repository;

import crypton.CryptoGuardians.domain.document.entity.DocumentShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentShareRepository extends JpaRepository<DocumentShare, Long> {
}
