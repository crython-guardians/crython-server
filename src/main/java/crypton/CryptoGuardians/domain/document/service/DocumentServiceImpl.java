package crypton.CryptoGuardians.domain.document.service;

import crypton.CryptoGuardians.domain.document.dto.AuthorizeResponseDTO;
import crypton.CryptoGuardians.domain.document.dto.DownloadResponseDTO;
import crypton.CryptoGuardians.domain.document.dto.ReportResponseDTO;
import crypton.CryptoGuardians.domain.document.dto.UploadRequestDTO;
import crypton.CryptoGuardians.domain.document.dto.ViewLogRequestDTO;
import crypton.CryptoGuardians.domain.document.entity.Document;
import crypton.CryptoGuardians.domain.document.entity.DocumentKey;
import crypton.CryptoGuardians.domain.document.entity.DocumentView;
import crypton.CryptoGuardians.domain.document.entity.User;
import crypton.CryptoGuardians.domain.document.repository.DocumentKeyRepository;
import crypton.CryptoGuardians.domain.document.repository.DocumentRepository;
import crypton.CryptoGuardians.domain.document.repository.DocumentViewRepository;
import crypton.CryptoGuardians.domain.document.repository.UserRepository;
import crypton.CryptoGuardians.global.error.exception.Exception404;
import crypton.CryptoGuardians.global.error.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentKeyRepository documentKeyRepository;
    private final DocumentViewRepository documentViewRepository;
    private final Path root = Paths.get("uploads");

    @Override
    public void saveFile(UploadRequestDTO uploadRequestDTO) {
        MultipartFile file = uploadRequestDTO.file();
        Long userId = uploadRequestDTO.uploadUserId();
        User uploadUser = userRepository.findById(userId).orElseThrow(() -> new Exception404("유저를 찾을 수 없습니다"));
        String originalFilename = file.getOriginalFilename();

        // 파일명 앞에 UUID 추가
        String uuid = UUID.randomUUID().toString();
        String newFilename = uuid + "_" + originalFilename;
        Path filePath = root.resolve(newFilename);

        try {
            // 디렉터리 존재 여부 확인 및 생성
            Files.createDirectories(root);

            // 실제 파일 디스크에 저장
            Files.copy(file.getInputStream(), filePath);
            // 파일 메타데이터 DB에 저장
            Document document = new Document(
                    newFilename,
                    formatFileSize(file.getSize()),
                    uploadUser,
                    filePath.toString(),
                    false
            );

            // documentKey 설정
            document.setDocumentKey(DocumentKey.createDocumentKey());

            documentRepository.save(document);
        } catch (IOException e) {
            throw new Exception500("파일 업로드에 실패했습니다.");
        }
    }

    @Override
    public DownloadResponseDTO loadFileAsResource(Long documentId) {
        Document document = findById(documentId);

        try {
            Path filePath = Paths.get(document.getFilePath()).toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                // 파일명에서 UUID 부분 슬라이싱
                String originalFileName = document.getFileName().substring(document.getFileName().indexOf("_") + 1);
                return new DownloadResponseDTO(originalFileName, resource);
            } else {
                throw new Exception404("파일을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            throw new Exception404("파일을 찾을 수 없습니다.");
        }
    }

    @Override
    public void deleteFile(Long documentId) {
        Document document = findById(documentId);

        try {
            Path filePath = Paths.get(document.getFilePath()).toAbsolutePath().normalize();
            // 실제 파일 삭제
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            // DB에서 파일 메타 데이터 삭제
            documentRepository.deleteById(documentId);
        } catch (IOException e) {
            throw new Exception500("파일 삭제에 실패했습니다.");
        }
    }

    @Override
    public Document findById(Long documentId) {
        return documentRepository.findById(documentId).orElseThrow(() -> new Exception404("파일을 찾을 수 없습니다."));
    }

    @Override
    public AuthorizeResponseDTO getAuthorizeKey(Long documentId) {
        DocumentKey documentKey = documentKeyRepository.findByDocumentId(documentId).orElseThrow(() -> new Exception404("파일 인증 키를 찾을 수 없습니다."));
        String key = documentKey.getAuthKey();
        return new AuthorizeResponseDTO(key);
    }

    @Override
    public ReportResponseDTO getReport(Long documentId) {
        Document document = documentRepository.findById(documentId).orElseThrow(() -> new Exception404("파일을 찾을 수 없습니다."));
        List<DocumentView> documentViews = documentViewRepository.findAllByDocumentId(documentId);
        return ReportResponseDTO.from(document, documentViews);
    }

    @Override
    public void saveViewLog(Long documentId, ViewLogRequestDTO viewLogRequestDTO) {
        Document document = documentRepository.findById(documentId).orElseThrow(() -> new Exception404("파일을 찾을 수 없습니다."));
        User viewer = userRepository.findById(viewLogRequestDTO.viewerId()).orElseThrow(() -> new Exception404("유저를 찾을 수 없습니다."));

        document.fileRead();
        documentRepository.save(document);

        DocumentView documentView = DocumentView.createDocumentView(document, viewer, viewLogRequestDTO.timestamp());
        documentViewRepository.save(documentView);
    }

    private String formatFileSize(long size) {
        if (size <= 0) return "0 KB";
        if (size < 1024) return "1 KB";

        final String[] units = new String[]{"KB", "MB"};
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
}
