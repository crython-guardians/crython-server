package crypton.CryptoGuardians.domain.document.repository;

import crypton.CryptoGuardians.domain.document.entity.Document;
import crypton.CryptoGuardians.domain.document.entity.DocumentKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentKeyRepository extends JpaRepository<DocumentKey, Long> {
}
