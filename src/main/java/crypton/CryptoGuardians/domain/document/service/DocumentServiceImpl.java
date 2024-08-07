package crypton.CryptoGuardians.domain.document.service;

import crypton.CryptoGuardians.domain.document.dto.AuthorizeResponseDTO;
import crypton.CryptoGuardians.domain.document.dto.UploadRequestDTO;
import crypton.CryptoGuardians.domain.document.entity.Document;
import crypton.CryptoGuardians.domain.document.entity.DocumentKey;
import crypton.CryptoGuardians.domain.document.repository.DocumentKeyRepository;
import crypton.CryptoGuardians.domain.document.repository.DocumentRepository;
import crypton.CryptoGuardians.global.error.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService{

    private final DocumentRepository documentRepository;
    private final DocumentKeyRepository documentKeyRepository;
    private final Path root = Paths.get("uploads");

    @Override
    public void saveFile(UploadRequestDTO uploadRequestDTO) throws IOException {
        MultipartFile file = uploadRequestDTO.file();
        String uploadUser = uploadRequestDTO.uploadUser();
        String fileName = file.getOriginalFilename();
        Path filePath = root.resolve(fileName);

        try {
            // 디렉터리 존재 여부 확인 및 생성
            Files.createDirectories(root);

            // 실제 파일 디스크에 저장
            Files.copy(file.getInputStream(), filePath);
            // 파일 메타데이터 DB에 저장
            Document document = new Document(
                    fileName,
                    formatFileSize(file.getSize()),
                    uploadUser,
                    filePath.toString(),
                    false
            );
            documentRepository.save(document);
        } catch (IOException e) {
            throw new IOException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    private String formatFileSize(long size) {
        if (size <= 0) return "0 KB";
        if (size < 1024) return "1 KB";

        final String[] units = new String[] { "KB", "MB" };
        int unitIndex = 0;
        double adjustedSize = size;

        // KB로 변환
        adjustedSize /= 1024.0;

        // 필요한 경우 MB로 변환
        if (adjustedSize >= 1024) {
            adjustedSize /= 1024.0;
            unitIndex = 1;
        }

        // 소수점 둘째자리까지 반올림
        DecimalFormat df = new DecimalFormat("#,##0.##");
        return df.format(adjustedSize) + " " + units[unitIndex];
    }

    @Override
    public AuthorizeResponseDTO getAuthorizeKey(Long documentId) {
        DocumentKey documentKey = documentKeyRepository.findByDocumentId(documentId).orElseThrow(() -> new Exception404("파일 인증 키를 찾을 수 없습니다."));
        String key = documentKey.getKey();
        return new AuthorizeResponseDTO(key);
    }
}